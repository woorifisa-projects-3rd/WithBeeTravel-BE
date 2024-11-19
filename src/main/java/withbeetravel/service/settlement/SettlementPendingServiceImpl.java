package withbeetravel.service.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.*;
import withbeetravel.repository.SettlementRequestLogRepository;
import withbeetravel.repository.SettlementRequestRepository;
import withbeetravel.repository.TravelMemberSettlementHistoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementPendingServiceImpl implements SettlementPendingService {

    private final SettlementRequestLogRepository settlementRequestLogRepository;
    private final TravelMemberSettlementHistoryRepository travelMemberSettlementHistoryRepository;
    private final SettlementRequestRepository settlementRequestRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePendingSettlementRequest(SettlementRequestLog settlementRequestLog,
                                               List<TravelMember> insufficientBalanceMembers,
                                               SettlementRequest settlementRequest,
                                               int updatedCount,
                                               TravelMemberSettlementHistory travelMemberSettlementHistory) {

        // 정산 보류 로그 저장
        settlementRequestLogRepository.save(settlementRequestLog);

        // 잔액 부족 멤버의 정산 동의를 true -> false로 변경
        for (TravelMember insufficientBalanceMember : insufficientBalanceMembers) {
            TravelMemberSettlementHistory insufficientTravelMemberSettlementHistory =
                    travelMemberSettlementHistoryRepository
                            .findTravelMemberSettlementHistoryBySettlementRequestIdAndTravelMemberId(
                                    settlementRequest.getId(), insufficientBalanceMember.getId());
            insufficientTravelMemberSettlementHistory.updateIsAgreed(false);
        }

        // 자신의 isAgreed는 true로 변경 (영속성 컨텍스트에서 관리하지 않기 때문에 수동으로 save 필요)
        travelMemberSettlementHistory.updateIsAgreed(true);
        travelMemberSettlementHistoryRepository.save(travelMemberSettlementHistory);

        // 정산 미동의 인원수 변경 (영속성 컨텍스트에서 관리하지 않기 때문에 수동으로 save 필요)
        System.out.println("insufficientBalanceMembers.size(): " + insufficientBalanceMembers.size());
        settlementRequest.updateDisagreeCount(insufficientBalanceMembers.size() - 1);
        settlementRequestRepository.save(settlementRequest);
    }
}
