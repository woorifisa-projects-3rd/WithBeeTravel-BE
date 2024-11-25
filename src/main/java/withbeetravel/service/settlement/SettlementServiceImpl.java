



package withbeetravel.service.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.*;
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
import withbeetravel.service.banking.AccountService;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private final SettlementPendingService settlementRequestLogService;
    private final AccountService accountService;

    @Override
    @Transactional(readOnly = true)
    public ShowSettlementDetailResponse getSettlementDetails(Long userId, Long travelId) {
        SettlementRequest settlementRequest = findSettlementRequestByTravelId(travelId);
        Long settlementRequestId = settlementRequest.getId();

        Long myTravelMemberId = findMyTravelMemberByTravelIdAndUserId(travelId, userId).getId();

        List<TravelMemberSettlementHistory> travelMemberSettlementHistories =
                travelMemberSettlementHistoryRepository.findAllBySettlementRequestId(settlementRequestId);

        // 미동의 인원수
        int disagreeCount = settlementRequest.getDisagreeCount();

        ShowMyTotalPaymentResponse myTotalPayments =
                createMyTotalPaymentResponse(userId, travelMemberSettlementHistories, myTravelMemberId);

        List<ShowOtherSettlementResponse> others =
                createOtherSettlementResponses(travelMemberSettlementHistories, myTravelMemberId);

        List<ShowMyDetailPaymentResponse> myDetailPayments =
                createMyDetailPaymentResponses(myTravelMemberId);

        return ShowSettlementDetailResponse.of(myTotalPayments, disagreeCount, myDetailPayments, others);
    }

    @Override
    public void requestSettlement(Long userId, Long travelId) {
        TravelMember travelMember = findMyTravelMemberByTravelIdAndUserId(travelId, userId);
        validateIsCaptain(travelMember);

        // 여행 멤버 인원수 카운트
        int totalMemberCount = travelMemberRepository.findAllByTravelId(travelId).size();

        Travel travel = findTravelById(travelId);

        // 진행 중인 정산 요청이 있을 경우 에러 처리
        validateSettlementStatusIsNotOngoing(travel);

        // 정산 요청 생성
        SettlementRequest newSettlementRequest = createSettlementRequest(travel, totalMemberCount);

        // 여행멤버정산내역 생성
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

        // 정산 여부를 ONGOING으로 변경
        travel.updateSettlementStatus(SettlementStatus.ONGOING);

        // 정산 요청 저장
        saveSettlementRequestLog(travel, LogTitle.SETTLEMENT_REQUEST);
    }

    private void validateSettlementStatusIsNotOngoing(Travel travel) {
        if (travel.getSettlementStatus().equals(SettlementStatus.ONGOING)) {
            throw new CustomException(SettlementErrorCode.SETTLEMENT_ONGOING_ALREADY_EXISTS);
        }
    }

    @Override
    public String agreeSettlement(Long userId, Long travelId) {
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
        User user = validateUser(userId);
        int myTotalPaymentCost =
                travelMemberSettlementHistory.getOwnPaymentCost() - travelMemberSettlementHistory.getActualBurdenCost();
        Account myConnectedAccount = user.getConnectedAccount();
        validateMyBalanceIsEnough(myTotalPaymentCost, myConnectedAccount);

        // disagree_count가 2 이상이면 isAgreed를 true로 변경, 1이면 정산 진행, 0이면 에러 발생
        int disagreeCount = settlementRequest.getDisagreeCount();

        if (disagreeCount >= 2) {
            updateIsAgreedAndDisagreeCount(travelMemberSettlementHistory, settlementRequest);

            return "정산 동의 완료";
        } else if (disagreeCount == 1) {

            // 총 정산 금액(ownPaymentCost - actualBurdenCost) 값의 오름차순으로 travelMemberSettlementHistory 리스트 정렬
            List<TravelMemberSettlementHistory> travelMemberSettlementHistories =
                    travelMemberSettlementHistoryRepository
                            .findAllBySettlementRequestIdOrderByCalculatedCost(settlementRequest.getId());

            // 총 정산 금액이 마이너스인 사람만 잔액이 있는지 확인
            // insufficientBalanceMembers : 잔액이 부족한 여행멤버 리스트
            List<TravelMember> insufficientBalanceMembers = new ArrayList<>();
            validateOtherMembersBalance(travelMemberSettlementHistories, insufficientBalanceMembers);

            // insufficientBalancedMembers에 한 명이라도 있을 경우, 정산 보류
            if (!insufficientBalanceMembers.isEmpty()) {

                // 정산 보류 로그 추가
                SettlementRequestLog settlementRequestLog =
                        saveSettlementRequestLog(travel, LogTitle.SETTLEMENT_PENDING);

                // 롤백시 실행되도록 다른 서비스 클래스로 분리
                settlementRequestLogService.handlePendingSettlementRequest(
                        settlementRequestLog,
                        insufficientBalanceMembers,
                        settlementRequest,
                        insufficientBalanceMembers.size(),
                        travelMemberSettlementHistory);

                throw new CustomException(SettlementErrorCode.SETTLEMENT_INSUFFICIENT_BALANCE);
            }

            // 나의 정산 동의 여부를 false -> true로 변경, disagreeCount에서 -1하기 (0으로 됨)
            updateIsAgreedAndDisagreeCount(travelMemberSettlementHistory, settlementRequest);

            // totalPaymentCost가 0보다 작은 경우, 해당 멤버의 계좌에서 totalPaymentCost를 인출
            // totalPaymentCost가 0 이상일 경우, 해당 멤버의 계좌에 totalPaymentCost를 송금
            Account managerAccount = accountRepository
                    .findById(9999L).orElseThrow(() -> new CustomException(BankingErrorCode.ACCOUNT_NOT_FOUND));

            // 총 정산 금액의 합산 계산
            int totalPaymentCostSum = getTotalPaymentCostSum(travelMemberSettlementHistories);

            // 위비트래블 계좌(관리자 계좌)의 잔액 부족 확인
            validateManagerBalance(managerAccount, totalPaymentCostSum);

            // 정산 처리
            processSettlement(travelMemberSettlementHistories, managerAccount, travel);

            // 정산 여부를 DONE으로 변경
            travel.updateSettlementStatus(SettlementStatus.DONE);

            // 정산 종료일을 현재로 변경
            settlementRequest.updateRequestEndDate(LocalDateTime.now());

            // 정산 완료 로그 생성
            SettlementRequestLog settlementRequestLog =
                    saveCompleteSettlementRequestLog(travel, totalPaymentCostSum);
            settlementRequestLogRepository.save(settlementRequestLog);

            return "모든 여행 멤버의 정산 동의 완료 후 정산 완료";
        }

        // 정산 미동의 인원수가 0인 경우 에러 처리
        else {
            throw new CustomException(SettlementErrorCode.SETTLEMENT_DISAGREE_COUNT_NOT_CERTAIN);
        }
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(AuthErrorCode.AUTHENTICATION_FAILED));
    }

    private void processSettlement(List<TravelMemberSettlementHistory> travelMemberSettlementHistories, Account managerAccount, Travel travel) {
        for (TravelMemberSettlementHistory settlementHistory : travelMemberSettlementHistories) {
            int totalPaymentCost = settlementHistory.getOwnPaymentCost() - settlementHistory.getActualBurdenCost();
            User user = settlementHistory.getTravelMember().getUser();
            Account connectedAccount = user.getConnectedAccount();
            if (totalPaymentCost < 0) {
                accountService.transfer(connectedAccount.getId(),
                        managerAccount.getAccountNumber(), -totalPaymentCost, "위비트래블 정산금 출금");
            } else {
                // 관리자 계좌의 송금 메시지를 "{여행명}의 정산금 출금"으로 표시
                accountService.transfer(managerAccount.getId(),
                        connectedAccount.getAccountNumber(), totalPaymentCost, travel.getTravelName() + "의 정산금 출금");
            }
        }
    }

    private int getTotalPaymentCostSum(List<TravelMemberSettlementHistory> travelMemberSettlementHistories) {
        int totalPaymentCostSum = 0;
        for (TravelMemberSettlementHistory settlementHistory : travelMemberSettlementHistories) {
            totalPaymentCostSum += settlementHistory.getOwnPaymentCost() - settlementHistory.getActualBurdenCost();
        }
        return totalPaymentCostSum;
    }

    private void validateManagerBalance(Account managerAccount, int totalPaymentCostSum) {
        if (managerAccount.getBalance() < totalPaymentCostSum) {
            throw new CustomException(BankingErrorCode.INSUFFICIENT_MANAGER_ACCOUNT_BALANCE);
        }
    }

    @Override
    public void cancelSettlement(Long userId, Long travelId) {

        // 정산 요청이 있는지 확인
        SettlementRequest settlementRequest = findSettlementRequestByTravelId(travelId);

        // 진행 중인 정산인지 확인
        Travel travel = findTravelById(travelId);
        validateSettlementRequestOngoing(travel);

        // 멤버들의 정산 내역을 먼저 삭제
        travelMemberSettlementHistoryRepository.deleteAllBySettlementRequestId(settlementRequest.getId());

        // 정산 요청 삭제
        settlementRequestRepository.deleteById(settlementRequest.getId());

        // 정산 여부를 ONGOING -> PENDING으로 변경
        travel.updateSettlementStatus(SettlementStatus.PENDING);

        // 정산 취소 로그 저장
        saveSettlementRequestLog(travel, LogTitle.SETTLEMENT_CANCEL);
    }

    private void updateIsAgreedAndDisagreeCount(TravelMemberSettlementHistory travelMemberSettlementHistory, SettlementRequest settlementRequest) {
        travelMemberSettlementHistory.updateIsAgreed(true);
        settlementRequest.updateDisagreeCount(-1);
    }

    private void validateOtherMembersBalance(List<TravelMemberSettlementHistory> travelMemberSettlementHistories, List<TravelMember> insufficientBalanceMembers) {
        for (TravelMemberSettlementHistory settlementHistory : travelMemberSettlementHistories) {
            int totalPaymentCost = settlementHistory.getOwnPaymentCost() - settlementHistory.getActualBurdenCost();

            if (totalPaymentCost < 0) {
                TravelMember travelMember = settlementHistory.getTravelMember();
                Account connectedAccount = travelMember.getUser().getConnectedAccount();

                if (connectedAccount.getBalance() + totalPaymentCost < 0) {
                    insufficientBalanceMembers.add(travelMember);
                }
            }
        }
    }

    private void validateMyBalanceIsEnough(int myTotalPaymentCost, Account myConnectedAccount) {
        if (myTotalPaymentCost < 0) {
            if (myConnectedAccount.getBalance() + myTotalPaymentCost < 0) {
                throw new CustomException(BankingErrorCode.INSUFFICIENT_FUNDS);
            }
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

    private SettlementRequestLog saveSettlementRequestLog(Travel travel,
                                                          LogTitle logTitle) {
        return settlementRequestLogRepository.save(
                SettlementRequestLog.builder()
                        .travel(travel)
                        .logTitle(logTitle)
                        .logMessage(logTitle.getMessage(travel.getTravelName()))
                        .build());
    }

    private SettlementRequestLog saveCompleteSettlementRequestLog(Travel travel, int additionalValue) {
        return settlementRequestLogRepository.save(
                SettlementRequestLog.builder()
                        .travel(travel)
                        .logTitle(LogTitle.SETTLEMENT_COMPLETE)
                        .logMessage( LogTitle.SETTLEMENT_COMPLETE.getMessage(travel.getTravelName(), additionalValue))
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
                    boolean isAgreed = history.isAgreed();
                    return ShowMyTotalPaymentResponse.of(name, isAgreed, ownPaymentCost, actualBurdenCost);
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
