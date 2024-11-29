package withbeetravel.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import withbeetravel.controller.notification.NotificationController;
import withbeetravel.domain.*;
import withbeetravel.dto.request.settlementRequestLog.SettlementRequestLogDto;
import withbeetravel.repository.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SettlementRequestLogServiceImpl implements SettlementRequestLogService{

    private final SettlementRequestLogRepository settlementRequestLogRepository;
    private final SettlementRequestRepository settlementRequestRepository;
    private final TravelRepository travelRepository;
    private final TravelMemberRepository travelMemberRepository;
    private final TravelMemberSettlementHistoryRepository travelMemberSettlementHistoryRepository;


    @Override
    public List<SettlementRequestLogDto> getSettlementRequestLogs(Long userId) {

        // 나의 모든 로그 리스트
        List<SettlementRequestLog> settlementRequestLogs = getSettlementRequestLogsByUserId(userId);

        // settlementRequestLogDto 리스트로 가공 (링크 추가)
        List<SettlementRequestLogDto> settlementRequestLogDtos = new ArrayList<>();
        for (SettlementRequestLog settlementRequestLog : settlementRequestLogs) {
            String link = settlementRequestLog.getLink();
            String title = settlementRequestLog.getLogTitle().getTitle();
            if (title.equals(LogTitle.SETTLEMENT_REQUEST) || title.equals(LogTitle.SETTLEMENT_RE_REQUEST)) {
                if (!settlementRequestRepository.existsByTravelId(settlementRequestLog.getTravel().getId())) {
                    link = null;
                }
            }

            SettlementRequestLogDto settlementRequestLogDto = SettlementRequestLogDto.of(settlementRequestLog, link);
            settlementRequestLogDtos.add(settlementRequestLogDto);
        }

        return settlementRequestLogDtos;
    }

    @Override
    public void createSettlementLogsForEndedTravels() {
        List<Travel> travels = travelRepository.findAllByTravelEndDate(LocalDate.now());
        for (Travel travel : travels) {
            List<TravelMember> travelMembers = travelMemberRepository.findAllByTravelId(travel.getId());
            travelMembers.forEach(travelMember -> {
                User user = travelMember.getUser();
                createSettlementRequestLog(travel, user, LogTitle.PAYMENT_REQUEST);
            });
        }
    }

    @Override
    public void createSettlementReRequestLogForNotAgreed(SettlementRequest settlementRequest) {
        Travel travel = settlementRequest.getTravel();
        travelMemberSettlementHistoryRepository.findAllBySettlementRequestId(settlementRequest.getId())
                .stream()
                .filter(travelMemberSettlementHistory -> !travelMemberSettlementHistory.isAgreed())
                .forEach(travelMemberSettlementHistory -> {
                    User user = travelMemberSettlementHistory.getTravelMember().getUser();
                    createSettlementRequestLog(travel, user, LogTitle.SETTLEMENT_RE_REQUEST);
                });
    }

    private void createSettlementRequestLog(Travel travel, User user, LogTitle logTitle) {
        SettlementRequestLog settlementRequestLog = SettlementRequestLog.builder()
                .travel(travel)
                .user(user)
                .logTitle(logTitle)
                .logMessage(logTitle.getMessage(travel.getTravelName()))
                .link(logTitle.getLinkPattern(travel.getId()))
                .build();
        settlementRequestLogRepository.save(settlementRequestLog);

        // 실시간 알림 전송
        String eventName = logTitle.equals(LogTitle.PAYMENT_REQUEST) ? "organizePayment" : "re-request";
        sendNotification(settlementRequestLog, eventName);
    }

    private List<SettlementRequestLog> getSettlementRequestLogsByUserId(Long userId) {
        return settlementRequestLogRepository.findAllByUserId(userId);
    }

    private void sendNotification(SettlementRequestLog settlementRequestLog, String eventName) {
        if (NotificationController.sseEmitters.containsKey(settlementRequestLog.getUser().getId())) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(settlementRequestLog.getUser().getId());
            try {
                if (sseEmitter != null) {
                    Map<String, String> eventData = new HashMap<>();
                    eventData.put("title", settlementRequestLog.getLogTitle().getTitle()); // 로그 타이틀 (ex. 정산 요청)
                    eventData.put("message", settlementRequestLog.getLogMessage()); // 로그 메시지
                    eventData.put("link", settlementRequestLog.getLink()); // 이동 링크

                    sseEmitter.send(SseEmitter.event().name(eventName).data(eventData));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

