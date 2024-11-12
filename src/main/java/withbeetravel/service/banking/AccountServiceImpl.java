package withbeetravel.service.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import withbeetravel.domain.Account;
import withbeetravel.domain.Product;
import withbeetravel.domain.User;
import withbeetravel.dto.banking.account.AccountRequest;
import withbeetravel.dto.banking.account.AccountResponse;
import withbeetravel.repository.AccountRepository;
import withbeetravel.repository.HistoryRepository;
import withbeetravel.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<AccountResponse> showAll(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts.stream().map(AccountResponse::from).collect(Collectors.toList());
    }

    public Account showAccount(Long accountId) {
        return accountRepository.findById(16L).orElseThrow();
    }

    //계좌 생성
    public Account createAccount(Long userId, AccountRequest accountRequest){
        User thisUser = userRepository.findById(userId).orElseThrow();

        String accountNumber = generateUniqueAccountNumber();


        Account account = Account.builder().user(thisUser)
                .accountNumber(accountNumber)
                .balance(0)
                .product(accountRequest.getProduct())
                .isConnectedWibeeCard(false)
                .build();

        return accountRepository.save(account);
    }

    // 유니크 계좌번호 생성
    public String generateUniqueAccountNumber() {
        String accountNumber;

        // 계좌번호가 중복되지 않도록 계속 생성
        do {
            accountNumber = generateAccountNumber();
        } while (isAccountNumberExists(accountNumber));

        return accountNumber;
    }

    public String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder("1"); // 첫 번째 자리는 항상 1

        // 나머지 부분 랜덤으로 생성 (12자리)
        for (int i = 0; i < 12; i++) {
            accountNumber.append(random.nextInt(10)); // 0-9 숫자 생성
        }

        return accountNumber.toString();
    }

    public boolean isAccountNumberExists(String accountNumber) {
        Optional<Account> existingAccount = accountRepository.findByAccountNumber(accountNumber);
        return existingAccount.isPresent(); // 존재하면 true 반환
    }
}

