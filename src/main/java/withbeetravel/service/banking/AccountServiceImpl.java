package withbeetravel.service.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import withbeetravel.domain.Account;
import withbeetravel.dto.banking.account.AccountResponse;
import withbeetravel.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;



    public List<AccountResponse> showAll(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts.stream().map(AccountResponse::from).collect(Collectors.toList());
    }

    @Override
    public Account showAccount(Long accountId) {
        return accountRepository.findById(16L).orElseThrow();
    }
}

