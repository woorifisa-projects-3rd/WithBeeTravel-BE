package withbeetravel.service.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.Account;
import withbeetravel.domain.History;
import withbeetravel.domain.User;
import withbeetravel.dto.request.account.HistoryRequest;
import withbeetravel.dto.response.account.HistoryResponse;
import withbeetravel.dto.response.account.WibeeCardHistoryListResponse;
import withbeetravel.dto.response.account.WibeeCardHistoryResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.exception.error.BankingErrorCode;
import withbeetravel.exception.error.ValidationErrorCode;
import withbeetravel.repository.AccountRepository;
import withbeetravel.repository.HistoryRepository;
import withbeetravel.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final AccountRepository accountRepository;

    public List<HistoryResponse> showAll(Long accountId) {
        List<History> histories = historyRepository.findByAccountIdOrderByDateDesc(accountId);
        List<HistoryResponse> historyResponses = histories.stream().map(HistoryResponse::from).toList();
        return historyResponses;
    }

    // 거래 내역 추가하기
    @Transactional
    public void addHistory(Long accountId, HistoryRequest historyRequest){

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
                account(account).date(LocalDateTime.now()).payAM(historyRequest.getPayAm())
                .rqspeNm(historyRequest.getRqspeNm()).isWibeeCard(historyRequest.isWibeeCard())
                .balance(account.getBalance()- historyRequest.getPayAm())
                .build();

        historyRepository.save(history);

        account.transfer(-historyRequest.getPayAm());
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

    void validateDateRange(LocalDate sDate, LocalDate eDate) {
        if (sDate.isAfter(eDate)) {
            throw new CustomException(ValidationErrorCode.DATE_RANGE_ERROR);
        }
    }
}
