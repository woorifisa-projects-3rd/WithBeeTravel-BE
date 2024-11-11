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

    public Account createAccount(Long userId, AccountRequest accountRequest){
        User thisUser = userRepository.findById(userId).orElseThrow();

        Account account = Account.builder().user(thisUser)
                .accountNumber(accountRequest.getAccountNumber())
                .balance(accountRequest.getBalance())
                .product(accountRequest.getProduct())
                .isConnectedWibeeCard(accountRequest.isConnected())
                .build();

        return accountRepository.save(account);
    }

}

