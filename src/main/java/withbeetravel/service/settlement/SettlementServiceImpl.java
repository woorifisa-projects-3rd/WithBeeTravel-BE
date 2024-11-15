package withbeetravel.service.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.SharedPayment;
import withbeetravel.domain.TravelMember;
import withbeetravel.domain.TravelMemberSettlementHistory;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.settlement.ShowMyDetailPaymentResponse;
import withbeetravel.dto.settlement.ShowMyTotalPaymentResponse;
import withbeetravel.dto.settlement.ShowOtherSettlementResponse;
import withbeetravel.dto.settlement.ShowSettlementDetailResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.SettlementErrorCode;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementServiceImpl implements SettlementService {

    private final TravelMemberRepository travelMemberRepository;
    private final SettlementRequestRepository settlementRequestRepository;
    private final TravelMemberSettlementHistoryRepository travelMemberSettlementHistoryRepository;
    private final PaymentParticipatedMemberRepository paymentParticipatedMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public SuccessResponse<ShowSettlementDetailResponse> getSettlementDetails(Long userId, Long travelId, Long settlementRequestId) {
        validateSettlement(settlementRequestId);
        Long myTravelMemberId = findMyTravelMemberIdByUserIdAndTravelId(userId, travelId);

        List<TravelMemberSettlementHistory> travelMemberSettlementHistories =
                travelMemberSettlementHistoryRepository.findAllBySettlementRequestId(settlementRequestId);

        ShowMyTotalPaymentResponse myTotalPayments =
                createMyTotalPaymentResponse(travelMemberSettlementHistories, myTravelMemberId);

        List<ShowOtherSettlementResponse> others =
                createOtherSettlementResponses(travelMemberSettlementHistories, myTravelMemberId);

        List<ShowMyDetailPaymentResponse> myDetailPayments =
                createMyDetailPaymentResponses(myTravelMemberId);

        ShowSettlementDetailResponse showSettlementDetailResponse = ShowSettlementDetailResponse.of(myTotalPayments, myDetailPayments, others);
        return SuccessResponse.of(HttpStatus.OK.value(), "세부 지출 내역 조회 성공", showSettlementDetailResponse);
    }

    private List<ShowMyDetailPaymentResponse> createMyDetailPaymentResponses(Long myTravelMemberId) {
        return paymentParticipatedMemberRepository.findAllByTravelMemberId(myTravelMemberId)
                .stream()
                .map(paymentParticipatedMember -> {
                    SharedPayment sharedPayment = paymentParticipatedMember.getSharedPayment();
                    Long id = sharedPayment.getId();
                    int participantCount = sharedPayment.getParticipantCount();
                    int paymentAmount = sharedPayment.getPaymentAmount();
                    int requestedAmount = paymentAmount / participantCount;

                    return ShowMyDetailPaymentResponse.of(
                            id,
                            paymentAmount,
                            requestedAmount,
                            sharedPayment.getStoreName(),
                            sharedPayment.getPaymentDate());
                })
                .toList();
    }

    private List<ShowOtherSettlementResponse> createOtherSettlementResponses(List<TravelMemberSettlementHistory> travelMemberSettlementHistories, Long myTravelMemberId) {
        return travelMemberSettlementHistories
                .stream()
                .filter(history -> !history.getTravelMember().getId().equals(myTravelMemberId))
                .map(history -> {
                    Long travelMemberId = history.getTravelMember().getId();
                    boolean isAgreed = history.isAgreed();
                    TravelMember travelMember = travelMemberRepository.findById(travelMemberId).orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_ACCESS_FORBIDDEN));
                    String memberName = travelMember.getUser().getName();
                    int totalPaymentCost = history.getOwnPaymentCost() - history.getActualBurdenCost();
                    return ShowOtherSettlementResponse.of(travelMemberId, memberName, totalPaymentCost, isAgreed);
                })
                .toList();
    }

    private ShowMyTotalPaymentResponse createMyTotalPaymentResponse(List<TravelMemberSettlementHistory> travelMemberSettlementHistories, Long myTravelMemberId) {
        return travelMemberSettlementHistories
                .stream()
                .filter(history -> history.getTravelMember().getId().equals(myTravelMemberId))
                .findFirst()
                .map(history -> {
                    int ownPaymentCost = history.getOwnPaymentCost();
                    int actualBurdenCost = history.getActualBurdenCost();
                    return ShowMyTotalPaymentResponse.of(ownPaymentCost, actualBurdenCost);
                })
                .orElseThrow(() -> new CustomException(SettlementErrorCode.MEMBER_SETTLEMENT_HISTORY_NOT_FOUND));
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