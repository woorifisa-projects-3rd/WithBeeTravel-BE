package withbeetravel.service.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.*;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.settlement.ShowMyDetailPaymentResponse;
import withbeetravel.dto.settlement.ShowMyTotalPaymentResponse;
import withbeetravel.dto.settlement.ShowOtherSettlementResponse;
import withbeetravel.dto.settlement.ShowSettlementDetailResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
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
    private final UserRepository userRepository;
    private final TravelRepository travelRepository;
    private final SharedPaymentRepository sharedPaymentRepository;

    @Override
    @Transactional(readOnly = true)
    public SuccessResponse<ShowSettlementDetailResponse> getSettlementDetails(Long userId, Long travelId) {
        Long settlementRequestId = findSettlementRequestIdByTravelId(travelId);

        Long myTravelMemberId = findMyTravelMemberIdByTravelIdAndUserId(travelId, userId).getId();

        List<TravelMemberSettlementHistory> travelMemberSettlementHistories =
                travelMemberSettlementHistoryRepository.findAllBySettlementRequestId(settlementRequestId);

        ShowMyTotalPaymentResponse myTotalPayments =
                createMyTotalPaymentResponse(userId, travelMemberSettlementHistories, myTravelMemberId);

        List<ShowOtherSettlementResponse> others =
                createOtherSettlementResponses(travelMemberSettlementHistories, myTravelMemberId);

        List<ShowMyDetailPaymentResponse> myDetailPayments =
                createMyDetailPaymentResponses(myTravelMemberId);

        ShowSettlementDetailResponse showSettlementDetailResponse =
                ShowSettlementDetailResponse.of(myTotalPayments, myDetailPayments, others);
        return SuccessResponse.of(HttpStatus.OK.value(), "세부 지출 내역 조회 성공", showSettlementDetailResponse);
    }

    @Override
    public SuccessResponse requestSettlement(Long userId, Long travelId) {
        TravelMember travelMember = findMyTravelMemberIdByTravelIdAndUserId(travelId, userId);
        validateIsCaptain(travelMember);

        int totalMemberCount = travelMemberRepository.findAllByTravelId(travelId).size();
        SettlementRequest newSettlementRequest =
                createSettlementRequest(travelId, totalMemberCount);

        for (TravelMember member : travelMemberRepository.findAllByTravelId(travelId)) {
            Long travelMemberId = member.getId();

            int ownPaymentCost = 0;
            for (SharedPayment sharedPayment :
                    sharedPaymentRepository.findAllByAddedByMemberId(travelMemberId)) {
                ownPaymentCost += sharedPayment.getPaymentAmount();
            }

            int actualBurdenCost = 0;
            for (PaymentParticipatedMember paymentParticipatedMember :
                    paymentParticipatedMemberRepository.findAllByTravelMemberId(travelMemberId)) {
                SharedPayment sharedPayment = paymentParticipatedMember.getSharedPayment();
                actualBurdenCost += sharedPayment.getPaymentAmount() / sharedPayment.getParticipantCount();
            }

            TravelMemberSettlementHistory travelMemberSettlementHistory =
                    TravelMemberSettlementHistory.builder()
                            .settlementRequest(newSettlementRequest)
                            .travelMember(member)
                            .ownPaymentCost(ownPaymentCost)
                            .actualBurdenCost(actualBurdenCost)
                            .isAgreed(false)
                            .build();

            travelMemberSettlementHistoryRepository.save(travelMemberSettlementHistory);
        }

        return SuccessResponse.of(HttpStatus.OK.value(), "정산 요청 성공");
    }

    private SettlementRequest createSettlementRequest(Long travelId, int totalMemberCount) {
        SettlementRequest newSettlementRequest = settlementRequestRepository.save(
                SettlementRequest.builder()
                        .travel(findTravelById(travelId))
                        .disagreeCount(totalMemberCount)
                        .build());
        return newSettlementRequest;
    }

    private Travel findTravelById(Long travelId) {
        return travelRepository
                .findById(travelId)
                .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_NOT_FOUND));
    }

    private void validateIsCaptain(TravelMember travelMember) {
        if (!travelMember.isCaptain()) {
            throw new CustomException(SettlementErrorCode.NO_PERMISSION_TO_MANAGE_SETTLEMENT);
        }
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

    private List<ShowOtherSettlementResponse> createOtherSettlementResponses
            (List<TravelMemberSettlementHistory> travelMemberSettlementHistories, Long myTravelMemberId) {
        return travelMemberSettlementHistories
                .stream()
                .filter(history -> !history.getTravelMember().getId().equals(myTravelMemberId))
                .map(history -> {
                    Long travelMemberId = history.getTravelMember().getId();
                    boolean isAgreed = history.isAgreed();
                    TravelMember travelMember = travelMemberRepository.findById(travelMemberId)
                            .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_ACCESS_FORBIDDEN));
                    String memberName = travelMember.getUser().getName();
                    int totalPaymentCost = history.getOwnPaymentCost() - history.getActualBurdenCost();
                    return ShowOtherSettlementResponse.of(travelMemberId, memberName, totalPaymentCost, isAgreed);
                })
                .toList();
    }

    private ShowMyTotalPaymentResponse createMyTotalPaymentResponse(
            Long userId, List<TravelMemberSettlementHistory> travelMemberSettlementHistories, Long myTravelMemberId) {
        return travelMemberSettlementHistories
                .stream()
                .filter(history -> history.getTravelMember().getId().equals(myTravelMemberId))
                .findFirst()
                .map(history -> {
                    String name = userRepository.findById(userId)
                            .orElseThrow(() -> new CustomException(AuthErrorCode.AUTHENTICATION_FAILED)).getName();
                    int ownPaymentCost = history.getOwnPaymentCost();
                    int actualBurdenCost = history.getActualBurdenCost();
                    return ShowMyTotalPaymentResponse.of(name, ownPaymentCost, actualBurdenCost);
                })
                .orElseThrow(() -> new CustomException(SettlementErrorCode.MEMBER_SETTLEMENT_HISTORY_NOT_FOUND));
    }

    private TravelMember findMyTravelMemberIdByTravelIdAndUserId(Long travelId, Long userId) {
        return travelMemberRepository
                .findByTravelIdAndUserId(travelId, userId)
                .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_ACCESS_FORBIDDEN));
    }

    private Long findSettlementRequestIdByTravelId(Long travelId) {
        SettlementRequest settlementRequest = settlementRequestRepository.findByTravelId(travelId)
                .orElseThrow(() -> new CustomException(SettlementErrorCode.SETTLEMENT_NOT_FOUND));
        return settlementRequest.getId();
    }
}