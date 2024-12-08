package withbeetravel.service.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.*;
import withbeetravel.dto.request.account.HistoryRequest;
import withbeetravel.dto.response.account.HistoryResponse;
import withbeetravel.dto.response.account.WibeeCardHistoryListResponse;
import withbeetravel.dto.response.account.WibeeCardHistoryResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.exception.error.BankingErrorCode;
import withbeetravel.exception.error.ValidationErrorCode;
import withbeetravel.repository.*;
import withbeetravel.service.payment.SharedPaymentRegisterService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final AccountRepository accountRepository;
    private final TravelMemberRepository travelMemberRepository;
    private final SharedPaymentRegisterService sharedPaymentRegisterService;

    public List<HistoryResponse> showAll(Long accountId) {
        List<History> histories = historyRepository.findByAccountIdOrderByDateDesc(accountId);
        List<HistoryResponse> historyResponses = histories.stream().map(HistoryResponse::from).toList();
        return historyResponses;
    }

    // 거래 내역 추가하기
    @Override
    @Transactional
    public void addHistory(
            Long userId,
            Long accountId,
            HistoryRequest historyRequest
    ){

        Account account = accountRepository.findById(accountId)
                .orElseThrow(()-> new CustomException(BankingErrorCode.ACCOUNT_NOT_FOUND));

        if(!account.isConnectedWibeeCard()){
            if(historyRequest.isWibeeCard()){ //위비 카드 연결되어있지 않았을 때, 위비 카드로결제했다하면 오류
                throw new CustomException(BankingErrorCode.WIBEE_CARD_NOT_ISSUED);
            }
        }

        if(account.getBalance()< historyRequest.getPayAm()){
            throw new CustomException(BankingErrorCode.INSUFFICIENT_FUNDS);
        }

        History history = History.builder().
                account(account)
                .date(LocalDateTime.now())
                .payAM(historyRequest.getPayAm())
                .rqspeNm(historyRequest.getRqspeNm())
                .isWibeeCard(historyRequest.isWibeeCard())
                .balance(account.getBalance()- historyRequest.getPayAm())
                .build();

        historyRepository.save(history);

        account.transfer(-historyRequest.getPayAm());

        // 위비 카드 결제 내역 & 여행 기간 중 발생한 결제 내역이면 공동 결제 내역에 자동으로 추가
        List<Travel> invitedTravelList = getInvitedTravelList(userId); // 참여 중인 여행 리스트

        Travel currentTravel = getCurrentTravels(invitedTravelList); // 현재 진행 중인 여행

        if(currentTravel != null) { // 현재 진행 중인 여행이 있다면, 해당 여행의 공동 결제 내역에 현재 결제 내역 추가

            sharedPaymentRegisterService.saveWibeeCardSharedPayment(
                    getTravelMember(userId, currentTravel.getId()),
                    currentTravel,
                    history
            );
            history.addedSharedPayment();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public WibeeCardHistoryListResponse getWibeeCardHistory(Long userId, String startDate, String endDate) {

        // User 엔티티 가져오기
        User user = getUser(userId);

        // 위비 카드를 발급받지 않은 회원인 경우 예외 처리
        Account account = user.getWibeeCardAccount();
        if(account == null)
            throw new CustomException(BankingErrorCode.WIBEE_CARD_NOT_ISSUED);

        // 날짜 필터링은 기본적으로 현재 시점으로부터 1달
        LocalDate eDate = LocalDate.now();
        LocalDate sDate = eDate.minusMonths(1);

        // 날짜 필터링 정보가 들어왔을 경우, 범위 체크 후 적용
        if((startDate != null && !startDate.isEmpty()) && (endDate != null && !endDate.isEmpty())) {
            validateDateRange(LocalDate.parse(startDate), LocalDate.parse(endDate));
            sDate = LocalDate.parse(startDate);
            eDate = LocalDate.parse(endDate);
        }

        // 결제 내역 가져오기
        List<History> histories = historyRepository.findByAccountIdAndDateBetween(account.getId(), sDate.atStartOfDay(), eDate.atStartOfDay().plusDays(1));

        // 위비 카드 결제 내역만 가져오기(날짜 오름차순 정렬)
        List<History> filteredHistories = histories.stream()
                .filter(History::isWibeeCard)
                .sorted((h1, h2) -> h1.getDate().compareTo(h2.getDate()))
                .toList();

        return WibeeCardHistoryListResponse.builder()
                .startDate(sDate.toString())
                .endDate(eDate.toString())
                .histories(filteredHistories.stream()
                        .map(WibeeCardHistoryResponse::from)
                        .toList())
                .build();
    }


    User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));
    }

    TravelMember getTravelMember(Long userId, Long travelId) {
        return travelMemberRepository.findByTravelIdAndUserId(travelId, userId).get();
    }

    // userId에 해당하는 회원이 참여 중인 여행 리스트 반환
    List<Travel> getInvitedTravelList(Long userId) {
        List<TravelMember> travelMembers = travelMemberRepository.findAllByUserId(userId);

        // TravelMember에서 Travel만 추출하여 리스트로 변환
        return travelMembers.stream()
                .map(TravelMember::getTravel) // TravelMember의 Travel 필드 추출
                .toList(); // List로 변환
    }

    // 현재 진행 중인 여행 반환
    public Travel getCurrentTravels(List<Travel> travels) {
        LocalDate today = LocalDate.now();

        for (Travel travel : travels) {
            if (!today.isBefore(travel.getTravelStartDate()) && !today.isAfter(travel.getTravelEndDate())) {
                return travel;  // 조건에 맞는 첫 번째 여행을 반환
            }
        }

        return null;
    }

    void validateDateRange(LocalDate sDate, LocalDate eDate) {
        if (sDate.isAfter(eDate)) {
            throw new CustomException(ValidationErrorCode.DATE_RANGE_ERROR);
        }
    }
}
