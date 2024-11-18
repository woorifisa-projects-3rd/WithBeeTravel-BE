package withbeetravel.service.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.*;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.settlement.ShowMyDetailPaymentResponse;
import withbeetravel.dto.response.settlement.ShowMyTotalPaymentResponse;
import withbeetravel.dto.response.settlement.ShowOtherSettlementResponse;
import withbeetravel.dto.response.settlement.ShowSettlementDetailResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.exception.error.SettlementErrorCode;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.repository.*;

import java.time.LocalDateTime;
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
    private final SettlementRequestLogRepository settlementRequestLogRepository;

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
    public SuccessResponse<Void> requestSettlement(Long userId, Long travelId) {
        TravelMember travelMember = findMyTravelMemberIdByTravelIdAndUserId(travelId, userId);
        validateIsCaptain(travelMember);

        int totalMemberCount = travelMemberRepository.findAllByTravelId(travelId).size();
        Travel travel = findTravelById(travelId);
        SettlementRequest newSettlementRequest = createSettlementRequest(travel, totalMemberCount);

        for (TravelMember member : travelMemberRepository.findAllByTravelId(travelId)) {
            Long travelMemberId = member.getId();

            int ownPaymentCost = getTotalOwnPaymentCost(travelMemberId);
            int actualBurdenCost = getTotalActualBurdenCost(travelMemberId);

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

        travel.updateSettlementStatus(SettlementStatus.ONGOING);

        saveSettlementRequestLog(travel, newSettlementRequest);

        return SuccessResponse.of(HttpStatus.OK.value(), "정산 요청 성공");
    }

    private void saveSettlementRequestLog(Travel travel, SettlementRequest newSettlementRequest) {

        settlementRequestLogRepository.save(
                SettlementRequestLog.builder()
                .settlementRequest(newSettlementRequest)
                .logTitle(LogTitle.SETTLEMENT_REQUEST)
                .logMessage(LogTitle.SETTLEMENT_REQUEST.getMessage(travel.getTravelName()))
                        .logTime(LocalDateTime.now())
                        .build());
    }

    private int getTotalActualBurdenCost(Long travelMemberId) {
        int actualBurdenCost = 0;
        for (PaymentParticipatedMember paymentParticipatedMember :
                paymentParticipatedMemberRepository.findAllByTravelMemberId(travelMemberId)) {
            SharedPayment sharedPayment = paymentParticipatedMember.getSharedPayment();
            actualBurdenCost += sharedPayment.getPaymentAmount() / sharedPayment.getParticipantCount();
        }
        return actualBurdenCost;
    }

    private int getTotalOwnPaymentCost(Long travelMemberId) {
        int ownPaymentCost = 0;
        for (SharedPayment sharedPayment :
                sharedPaymentRepository.findAllByAddedByMemberId(travelMemberId)) {
            ownPaymentCost += sharedPayment.getPaymentAmount();
        }
        return ownPaymentCost;
    }

    private SettlementRequest createSettlementRequest(Travel travel, int totalMemberCount) {
        SettlementRequest newSettlementRequest = settlementRequestRepository.save(
                SettlementRequest.builder()
                        .travel(travel)
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