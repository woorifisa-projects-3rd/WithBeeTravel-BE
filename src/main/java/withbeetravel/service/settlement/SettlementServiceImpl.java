package withbeetravel.service.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import withbeetravel.domain.SharedPayment;
import withbeetravel.domain.TravelMember;
import withbeetravel.domain.TravelMemberSettlementHistory;
import withbeetravel.dto.settlement.ShowMyDetailPaymentResponse;
import withbeetravel.dto.settlement.ShowMyTotalPaymentResponse;
import withbeetravel.dto.settlement.ShowOtherSettlementResponse;
import withbeetravel.dto.settlement.ShowSettlementDetailResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.SettlementErrorCode;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService{

    private final TravelMemberRepository travelMemberRepository;
    private final SettlementRequestRepository settlementRequestRepository;
    private final TravelMemberSettlementHistoryRepository travelMemberSettlementHistoryRepository;
    private final PaymentParticipatedMemberRepository paymentParticipatedMemberRepository;

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

        List<ShowOtherSettlementResponse> others =
                travelMemberSettlementHistories
                        .stream()
                        .filter(history -> !history.getTravelMember().getId().equals(myTravelMemberId))
                        .map(history -> {
                            Long travelMemberId = history.getTravelMember().getId();
                            TravelMember travelMember = travelMemberRepository.findById(travelMemberId).orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_ACCESS_FORBIDDEN));
                            String memberName = travelMember.getUser().getName();
                            int totalPaymentCost = history.getOwnPaymentCost() - history.getActualBurdenCost();
                            return ShowOtherSettlementResponse.of(memberName, totalPaymentCost);
                        })
                        .collect(Collectors.toList());

        List<ShowMyDetailPaymentResponse> myDetailPayments =
                paymentParticipatedMemberRepository.findAllByTravelMemberId(myTravelMemberId)
                        .stream()
                        .map(paymentParticipatedMember -> {
                            SharedPayment sharedPayment = paymentParticipatedMember.getSharedPayment();
                            int participantCount = sharedPayment.getParticipantCount();
                            int paymentAmount = sharedPayment.getPaymentAmount();
                            int requestedAmount = paymentAmount/participantCount;

                            return ShowMyDetailPaymentResponse.of(
                                    paymentAmount,
                                    requestedAmount,
                                    sharedPayment.getStoreName(),
                                    sharedPayment.getPaymentDate());
                        })
                        .collect(Collectors.toList());

        return ShowSettlementDetailResponse.of(myTotalPayments, myDetailPayments, others);
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
