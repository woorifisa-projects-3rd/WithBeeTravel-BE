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
import withbeetravel.exception.error.BankingErrorCode;
import withbeetravel.exception.error.SettlementErrorCode;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.repository.*;

import java.util.ArrayList;
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
    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public SuccessResponse<ShowSettlementDetailResponse> getSettlementDetails(Long userId, Long travelId) {
        Long settlementRequestId = findSettlementRequestByTravelId(travelId).getId();

        Long myTravelMemberId = findMyTravelMemberByTravelIdAndUserId(travelId, userId).getId();

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
        TravelMember travelMember = findMyTravelMemberByTravelIdAndUserId(travelId, userId);
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

    @Override
    public SuccessResponse<Void> agreeSettlement(Long userId, Long travelId) {
        // 해당 멤버가 여행 멤버인지 확인
        TravelMember selfTravelMember = findMyTravelMemberByTravelIdAndUserId(travelId, userId);

        // 정산 요청의 존재 여부 및 정산 여부가 ONGOING(진행중)인지 확인
        SettlementRequest settlementRequest = findSettlementRequestByTravelId(travelId);
        Travel travel = findTravelById(travelId);
        validateSettlementRequestOngoing(travel);

        // 해당 멤버가 이미 동의한 정산 요청인지 확인
        TravelMemberSettlementHistory travelMemberSettlementHistory =
                travelMemberSettlementHistoryRepository
                        .findTravelMemberSettlementHistoryBySettlementRequestIdAndTravelMemberId(
                                settlementRequest.getId(), selfTravelMember.getId());
        validateSettlementRequestAlreadyAgree(travelMemberSettlementHistory);

        // 나의 총 정산 금액이 마이너스인 경우, 잔액이 부족하지 않은 지 확인
        // 연결 계좌가 유효한지 확인하는 로직 추가 필요할까?
        int myTotalPaymentCost =
                travelMemberSettlementHistory.getOwnPaymentCost() - travelMemberSettlementHistory.getActualBurdenCost();
        Account myConnectedAccount = selfTravelMember.getConnectedAccount();
        if (myTotalPaymentCost < 0) {
            if (myConnectedAccount.getBalance() + myTotalPaymentCost < 0) {
                throw new CustomException(BankingErrorCode.INSUFFICIENT_FUNDS);
            }
        }

        // disagree_count가 2 이상이면 isAgreed를 true로 변경, 1이면 정산 진행
        int disagreeCount = settlementRequest.getDisagreeCount();
        if (disagreeCount >= 2) {
            travelMemberSettlementHistory.updateIsAgreed(true);
            settlementRequest.updateDisagreeCount(-1);
            return SuccessResponse.of(HttpStatus.OK.value(), "정산 동의 완료");

        } else if (disagreeCount == 1) {

            // 총 정산 금액(ownPaymentCost - actualBurdenCost) 값의 오름차순으로 travelMemberSettlementHistory 리스트 정렬
            List<TravelMemberSettlementHistory> travelMemberSettlementHistories =
                    travelMemberSettlementHistoryRepository
                            .findAllBySettlementRequestIdOrderByCalculatedCost(settlementRequest.getId());

            // 총 정산 금액이 마이너스인 사람만 잔액이 있는지 확인
            // insufficientBalanceMembers : 잔액이 부족한 여행멤버 리스트
            List<TravelMember> insufficientBalanceMembers = new ArrayList<>(List.of());
            for (TravelMemberSettlementHistory settlementHistory : travelMemberSettlementHistories) {
                int totalPaymentCost = settlementHistory.getOwnPaymentCost() - settlementHistory.getActualBurdenCost();

                if (totalPaymentCost < 0) {
                    TravelMember travelMember = settlementHistory.getTravelMember();
                    Account connectedAccount = travelMember.getConnectedAccount();

                    if (connectedAccount.getBalance() + totalPaymentCost < 0) {
                        insufficientBalanceMembers.add(travelMember);
                    }
                }
            }

            System.out.println(insufficientBalanceMembers.toString());

            // insufficientBalancedMembers에 한 명이라도 있을 경우, 정산 보류
            if (!insufficientBalanceMembers.isEmpty()) {
                // 정산 보류 로그 추가
                SettlementRequestLog settlementRequestLog = SettlementRequestLog
                        .builder()
                        .logTitle(LogTitle.SETTLEMENT_PENDING)
                        .logMessage(LogTitle.SETTLEMENT_PENDING.getMessage(travel.getTravelName()))
                        .settlementRequest(settlementRequest)
                        .build();
                settlementRequestLogRepository.save(settlementRequestLog);
                System.out.println(settlementRequestLog.toString());

                // 잔액 부족 멤버의 정산 동의를 true -> false로 변경
                for (TravelMember insufficientBalanceMember : insufficientBalanceMembers) {
                    TravelMemberSettlementHistory insufficientTravelMemberSettlementHistory =
                            travelMemberSettlementHistoryRepository
                                    .findTravelMemberSettlementHistoryBySettlementRequestIdAndTravelMemberId(
                                    settlementRequest.getId(), insufficientBalanceMember.getId());
                    insufficientTravelMemberSettlementHistory.updateIsAgreed(false);
                }

                // 정산 미동의 인원수 변경
                settlementRequest.updateDisagreeCount(insufficientBalanceMembers.size());

                throw(new CustomException(SettlementErrorCode.SETTLEMENT_INSUFFICIENT_BALANCE));
            }

            // 잔액 부족 멤버가 없을 경우 나의 정산 동의 상태를 true로 변경, 정산 미동의 인원에서 1을 뺌
            else {
                travelMemberSettlementHistory.updateIsAgreed(true);
                settlementRequest.updateDisagreeCount(-1);
            }

            // 모든 그룹원들의 계좌 잔액 
            // totalPaymentCost가 0보다 작은 경우, 해당 멤버의 계좌에서 totalPaymentCost를 인출
            // totalPaymentCost가 0 이상일 경우, 해당 멤버의 계좌에 totalPaymentCost를 송금
            // 여기서도 연결 계좌 유효한지 확인 필요할까?
            for (TravelMemberSettlementHistory settlementHistory : travelMemberSettlementHistories) {
                int totalPaymentCost = settlementHistory.getOwnPaymentCost() - settlementHistory.getActualBurdenCost();
                Account connectedAccount = settlementHistory.getTravelMember().getConnectedAccount();
                if (totalPaymentCost < 0) {
                    connectedAccount.transfer(totalPaymentCost);
                } else {
                    connectedAccount.transfer(totalPaymentCost);
                }
            }

            // 정산 여부를 DONE으로 변경
            travel.updateSettlementStatus(SettlementStatus.DONE);

            // 정산 완료 로그 생성
            SettlementRequestLog settlementRequestLog = SettlementRequestLog
                    .builder()
                    .logTitle(LogTitle.SETTLEMENT_COMPLETE)
                    .logMessage(LogTitle.SETTLEMENT_COMPLETE.getMessage(travel.getTravelName()))
                    .settlementRequest(settlementRequest)
                    .build();
            settlementRequestLogRepository.save(settlementRequestLog);

            return SuccessResponse.of(HttpStatus.OK.value(), "모든 여행 멤버의 정산 동의 완료 후 정산 완료");
        }

        // 정산 미동의 인원수가 0인 경우 에러 처리
        else {
            throw new CustomException(SettlementErrorCode.SETTLEMENT_DISAGREE_COUNT_NOT_CERTAIN);
        }
    }

    private void validateSettlementRequestAlreadyAgree(TravelMemberSettlementHistory travelMemberSettlementHistory) {
        if (travelMemberSettlementHistory.isAgreed()) {
            throw new CustomException(SettlementErrorCode.SETTLEMENT_ALREADY_AGREED);
        }
    }

    private void validateSettlementRequestOngoing(Travel travel) {
        if (!travel.getSettlementStatus().equals(SettlementStatus.ONGOING)) {
            throw new CustomException(SettlementErrorCode.SETTLEMENT_NOT_ONGOING);
        }
    }

    private void saveSettlementRequestLog(Travel travel, SettlementRequest newSettlementRequest) {

        settlementRequestLogRepository.save(
                SettlementRequestLog.builder()
                        .settlementRequest(newSettlementRequest)
                        .logTitle(LogTitle.SETTLEMENT_REQUEST)
                        .logMessage(LogTitle.SETTLEMENT_REQUEST.getMessage(travel.getTravelName()))
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

    private TravelMember findMyTravelMemberByTravelIdAndUserId(Long travelId, Long userId) {
        return travelMemberRepository
                .findByTravelIdAndUserId(travelId, userId)
                .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_ACCESS_FORBIDDEN));
    }

    private SettlementRequest findSettlementRequestByTravelId(Long travelId) {
        return settlementRequestRepository.findByTravelId(travelId)
                .orElseThrow(() -> new CustomException(SettlementErrorCode.SETTLEMENT_NOT_FOUND));
    }
}
