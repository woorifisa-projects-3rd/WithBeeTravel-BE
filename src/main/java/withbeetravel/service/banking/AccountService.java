package withbeetravel.service.banking;

import withbeetravel.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<Account> showAll(Long userId);

    Account showAccount(Long accountId);
}
