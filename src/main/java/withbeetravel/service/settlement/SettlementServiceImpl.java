package withbeetravel.service.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import withbeetravel.domain.TravelMember;
import withbeetravel.domain.TravelMemberSettlementHistory;
import withbeetravel.dto.settlement.ShowMyTotalPaymentResponse;
import withbeetravel.dto.settlement.ShowSettlementDetailResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.SettlementErrorCode;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService{

    private final TravelMemberRepository travelMemberRepository;
    private final SettlementRequestRepository settlementRequestRepository;
    private final TravelMemberSettlementHistoryRepository travelMemberSettlementHistoryRepository;

    @Override
    public ShowSettlementDetailResponse getSettlementDetails(Long userId, Long travelId, Long settlementRequestId) {
        validateSettlement(settlementRequestId);
        Long myTravelMemberId = findMyTravelMemberIdByUserIdAndTravelId(userId, travelId);

        List<TravelMemberSettlementHistory> travelMemberSettlementHistories =
                travelMemberSettlementHistoryRepository.findAllBySettlementRequestId(settlementRequestId);

        ShowMyTotalPaymentResponse myTotalPayments =
                travelMemberSettlementHistories
                        .stream()
                        .filter(history -> history.getTravelMember().getId().equals(myTravelMemberId))
                        .findFirst()
                        .map(history -> {
                            int ownPaymentCost = history.getOwnPaymentCost();
                            int actualBurdenCost = history.getActualBurdenCost();
                            int totalPaymentCost = ownPaymentCost - actualBurdenCost;

                            return ShowMyTotalPaymentResponse.of(totalPaymentCost, ownPaymentCost, actualBurdenCost);
                        })
                        .orElseThrow(() -> new CustomException(SettlementErrorCode.MEMBER_SETTLEMENT_HISTORY_NOT_FOUND));

        return null;
    }

    private Long findMyTravelMemberIdByUserIdAndTravelId(Long userId, Long travelId) {
        TravelMember userTravelMember =
                travelMemberRepository.findByUserIdAndTravelId(userId, travelId).orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_ACCESS_FORBIDDEN));
        return userTravelMember.getId();
    }

    private void validateSettlement(Long settlementRequestId) {
        settlementRequestRepository.findById(settlementRequestId).orElseThrow(() -> new CustomException(SettlementErrorCode.SETTLEMENT_NOT_FOUND));
    }
}