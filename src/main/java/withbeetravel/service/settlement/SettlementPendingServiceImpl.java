package withbeetravel.service.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import withbeetravel.controller.notification.NotificationController;
import withbeetravel.domain.*;
import withbeetravel.repository.SettlementRequestLogRepository;
import withbeetravel.repository.SettlementRequestRepository;
import withbeetravel.repository.TravelMemberSettlementHistoryRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SettlementPendingServiceImpl implements SettlementPendingService {

    private final SettlementRequestLogRepository settlementRequestLogRepository;
    private final TravelMemberSettlementHistoryRepository travelMemberSettlementHistoryRepository;
    private final SettlementRequestRepository settlementRequestRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePendingSettlementRequest(List<SettlementRequestLog> settlementRequestLogs,
                                               List<TravelMember> insufficientBalanceMembers,
                                               SettlementRequest settlementRequest,
                                               int updatedCount,
                                               TravelMemberSettlementHistory travelMemberSettlementHistory) {

        // 정산 보류 로그 저장
        settlementRequestLogRepository.saveAll(settlementRequestLogs);

        // 실시간 알림 전송
        sendNotification(settlementRequestLogs);

        // 잔액 부족 멤버의 정산 동의를 true -> false로 변경
        changeIsAgreedToFalse(insufficientBalanceMembers, settlementRequest);

        // 자신의 isAgreed는 true로 변경 (영속성 컨텍스트에서 관리하지 않기 때문에 수동으로 save 필요)
        travelMemberSettlementHistory.updateIsAgreed(true);
        travelMemberSettlementHistoryRepository.save(travelMemberSettlementHistory);

        // 정산 미동의 인원수 변경 (영속성 컨텍스트에서 관리하지 않기 때문에 수동으로 save 필요)
        settlementRequest.updateDisagreeCount(insufficientBalanceMembers.size() - 1);
        settlementRequestRepository.save(settlementRequest);
    }

    private void changeIsAgreedToFalse(List<TravelMember> insufficientBalanceMembers, SettlementRequest settlementRequest) {
        for (TravelMember insufficientBalanceMember : insufficientBalanceMembers) {
            TravelMemberSettlementHistory insufficientTravelMemberSettlementHistory =
                    travelMemberSettlementHistoryRepository
                            .findTravelMemberSettlementHistoryBySettlementRequestIdAndTravelMemberId(
                                    settlementRequest.getId(), insufficientBalanceMember.getId());
            insufficientTravelMemberSettlementHistory.updateIsAgreed(false);
        }
    }

    private void sendNotification(List<SettlementRequestLog> settlementRequestLogs) {
        for (SettlementRequestLog settlementRequestLog : settlementRequestLogs) {
            Long userId = settlementRequestLog.getUser().getId();
            if (NotificationController.sseEmitters.containsKey(userId)) {
                SseEmitter sseEmitter = NotificationController.sseEmitters.get(userId);
                try {
                    if (sseEmitter != null) {
                        Map<String, String> eventData = new HashMap<>();
                        eventData.put("title", settlementRequestLog.getLogTitle().getTitle()); // 로그 타이틀 (정산 보류)
                        eventData.put("message", settlementRequestLog.getLogMessage()); // 로그 메시지
                        eventData.put("link", settlementRequestLog.getLink()); // 이동 링크

                        sseEmitter.send(SseEmitter.event().name("pending").data(eventData));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
