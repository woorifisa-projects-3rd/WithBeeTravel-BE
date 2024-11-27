package withbeetravel.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import withbeetravel.domain.*;
import withbeetravel.dto.request.settlementRequestLog.SettlementRequestLogDto;
import withbeetravel.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            String link = createLink(settlementRequestLog);
            SettlementRequestLogDto settlementRequestLogDto = createSettlementRequestLogDto(settlementRequestLog, link);
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
                .build();
        settlementRequestLogRepository.save(settlementRequestLog);
    }


    private SettlementRequestLogDto createSettlementRequestLogDto(SettlementRequestLog settlementRequestLog, String link) {
        return SettlementRequestLogDto.builder().id(settlementRequestLog.getId())
                .logTime(settlementRequestLog.getLogTime())
                .logTitle(settlementRequestLog.getLogTitle().getTitle())
                .logMessage(settlementRequestLog.getLogMessage())
                .link(link)
                .build();
    }

    private List<SettlementRequestLog> getSettlementRequestLogsByUserId(Long userId) {
        return settlementRequestLogRepository.findAllByUserId(userId);
    }

    private String createLink(SettlementRequestLog settlementRequestLog) {
        String link = null;
        Long travelId = settlementRequestLog.getTravel().getId();
        LogTitle logTitle = settlementRequestLog.getLogTitle();
        if (logTitle.equals(LogTitle.PAYMENT_REQUEST)) {
            link = "travel/" + travelId + "/payments";
        } else if (logTitle.equals(LogTitle.SETTLEMENT_REQUEST) || logTitle.equals(LogTitle.SETTLEMENT_RE_REQUEST)) {
            if (settlementRequestRepository.existsByTravelId(travelId)) {
                link = "travel/" + travelId + "/settlement";
            }
        } else if (logTitle.equals(LogTitle.SETTLEMENT_PENDING)) {
            Long accountId = settlementRequestLog.getUser().getConnectedAccount().getId();
            link = "banking/" + accountId;
        }
        return link;
    }
}

