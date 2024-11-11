package withbeetravel.service.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import withbeetravel.domain.Account;
import withbeetravel.repository.AccountRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;



    public List<Account> showAll(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public Account showAccount(Long accountId) {
        return accountRepository.findById(16L).orElseThrow();
    }
}

