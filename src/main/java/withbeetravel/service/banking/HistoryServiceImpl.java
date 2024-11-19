package withbeetravel.service.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.Account;
import withbeetravel.domain.History;
import withbeetravel.dto.request.account.HistoryRequest;
import withbeetravel.dto.response.account.HistoryResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.BankingErrorCode;
import withbeetravel.repository.AccountRepository;
import withbeetravel.repository.HistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final AccountRepository accountRepository;

    public SuccessResponse<List<HistoryResponse>> showAll(Long accountId) {
        List<History> histories = historyRepository.findByAccountIdOrderByDateDesc(accountId);
        List<HistoryResponse> historyResponses = histories.stream().map(HistoryResponse::from).toList();
        return SuccessResponse.of(
                HttpStatus.OK.value(),
                "계좌 거래내역 조회 성공",
                historyResponses
        );
    }

    // 거래 내역 추가하기
    @Transactional
    public SuccessResponse addHistory(Long accountId, HistoryRequest historyRequest){

        Account account = accountRepository.findById(accountId)
                .orElseThrow(()-> new CustomException(BankingErrorCode.ACCOUNT_NOT_FOUND));

        if(!account.isConnectedWibeeCard()){
            if(historyRequest.isWibeeCard()){ //위비 카드 연결되어있지 않았을 때, 위비 카드로결제했다하면 오류
                throw new CustomException(BankingErrorCode.NOT_CONNECTED_WIBEE_ACCOUNT);
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

        return SuccessResponse.of(
                HttpStatus.CREATED.value(),
                "결제 내역 등록 완료"
        );
    }




}
