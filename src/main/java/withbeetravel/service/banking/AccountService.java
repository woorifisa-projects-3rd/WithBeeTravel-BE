package withbeetravel.service.banking;

import withbeetravel.domain.Account;
import withbeetravel.dto.banking.account.AccountRequest;
import withbeetravel.dto.banking.account.AccountResponse;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<AccountResponse> showAll(Long userId);

    Account showAccount(Long accountId);

    Account createAccount(Long userId, AccountRequest accountRequest);

    String generateUniqueAccountNumber();

    String generateAccountNumber();

    boolean isAccountNumberExists(String accountNumber);

    void transfer(Long accountId, String accountNumber, int amount);
}
