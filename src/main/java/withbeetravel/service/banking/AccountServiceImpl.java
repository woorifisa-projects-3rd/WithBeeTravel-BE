package withbeetravel.service.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.Account;
import withbeetravel.domain.History;
import withbeetravel.domain.Product;
import withbeetravel.domain.User;
import withbeetravel.dto.banking.account.AccountRequest;
import withbeetravel.dto.banking.account.AccountResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.BankingErrorCode;
import withbeetravel.repository.AccountRepository;
import withbeetravel.repository.HistoryRepository;
import withbeetravel.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    // 계좌 조회
    public List<AccountResponse> showAll(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts.stream().map(AccountResponse::from).collect(Collectors.toList());
    }

    // 계좌 내역 조회
    public Account showAccount(Long accountId) {
        return accountRepository.findById(16L).orElseThrow(()->new CustomException(BankingErrorCode.ACCOUNT_NOT_FOUND));
    }

    //계좌 생성
    public SuccessResponse<AccountResponse> createAccount(Long userId, AccountRequest accountRequest){
        User thisUser = userRepository.findById(userId).orElseThrow();

        String accountNumber = generateUniqueAccountNumber();

        Account account = Account.builder().user(thisUser)
                .accountNumber(accountNumber)
                .balance(0)
                .product(accountRequest.getProduct())
                .isConnectedWibeeCard(false)
                .build();

        accountRepository.save(account);

        AccountResponse accountResponse = AccountResponse.from(account);

        SuccessResponse<AccountResponse> response  = SuccessResponse.of(
                HttpStatus.CREATED.value(),
                "계좌 생성 완료",
                accountResponse
        );

        return response;
    }

    // 유니크 계좌번호 확인
    public String generateUniqueAccountNumber() {
        String accountNumber;

        // 계좌번호가 중복되지 않도록 계속 생성
        do {
            accountNumber = generateAccountNumber();
        } while (isAccountNumberExists(accountNumber));

        return accountNumber;
    }

    // 계좌번호 랜덤 생성
    public String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder("1"); // 첫 번째 자리는 항상 1

        // 나머지 부분 랜덤으로 생성 (12자리)
        for (int i = 0; i < 12; i++) {
            accountNumber.append(random.nextInt(10)); // 0-9 숫자 생성
        }

        return accountNumber.toString();
    }

    // 계좌번호 존재 유무 확인
    public boolean isAccountNumberExists(String accountNumber) {
        Optional<Account> existingAccount = accountRepository.findByAccountNumber(accountNumber);
        return existingAccount.isPresent(); // 존재하면 true 반환
    }

    // 송금하기
    @Transactional
    public void transfer(Long accountId, String accountNumber, int amount, String rqspeNm) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(()-> new CustomException(BankingErrorCode.ACCOUNT_NOT_FOUND));

        Account targetAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new CustomException(BankingErrorCode.ACCOUNT_NOT_FOUND));

        if(amount > account.getBalance()){
            throw new CustomException(BankingErrorCode.INSUFFICIENT_FUNDS);
        }

        // 출금 처리

        // 계좌 내역 객체 생성
        History newHistory = History.builder().account(account).payAM(amount).rqspeNm(rqspeNm)
                .date(LocalDateTime.now()).balance(account.getBalance()-amount).isWibeeCard(false).build();
        // 후 저장 처리
        historyRepository.save(newHistory);
        // 한 다음 계좌 금액 조절
        account.transfer(-amount);



        // 타겟 계좌 입금 처리

        // 타겟 계좌 내역 객체 생성, 저장
        History targetHistory = History.builder().account(targetAccount).rcvAm(amount).rqspeNm(account.getUser().getName())
                .date(LocalDateTime.now()).balance(targetAccount.getBalance()+amount).isWibeeCard(false).build();
        historyRepository.save(targetHistory);

        // 상대 계좌 입금 처리
        targetAccount.transfer(amount);
    }

    @Transactional
    public void deposit(Long accountId, int amount, String rqspeNm) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(()-> new CustomException(BankingErrorCode.ACCOUNT_NOT_FOUND));

        History history = History.builder().account(account).balance(account.getBalance()+amount).rcvAm(amount)
                        .date(LocalDateTime.now()).rqspeNm(rqspeNm).isWibeeCard(false).build();

        historyRepository.save(history);

        account.transfer(amount);

    }

    // accountId로 계좌 조회하기
    public AccountResponse accountInfo(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(()->new CustomException(BankingErrorCode.ACCOUNT_NOT_FOUND));

        return  AccountResponse.from(account);
    }

    public boolean verifyAccount(String accountNumber) {
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        if(account.isEmpty()){
            return false;
        }
        return true;
    }

    public String findUserNameByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow();
        String name = account.getUser().getName();
        return name;
    }
}

