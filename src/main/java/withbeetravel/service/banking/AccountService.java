package withbeetravel.service.banking;

import withbeetravel.domain.Account;
import withbeetravel.dto.banking.account.AccountResponse;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<AccountResponse> showAll(Long userId);

    Account showAccount(Long accountId);
}
