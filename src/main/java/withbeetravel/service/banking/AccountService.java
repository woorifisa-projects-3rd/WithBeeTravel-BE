package withbeetravel.service.banking;

import withbeetravel.domain.Account;
import withbeetravel.dto.banking.account.AccountOwnerNameResponse;
import withbeetravel.dto.banking.account.AccountRequest;
import withbeetravel.dto.banking.account.AccountResponse;
import withbeetravel.dto.response.SuccessResponse;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    SuccessResponse<List<AccountResponse>> showAll(Long userId);

    SuccessResponse<AccountResponse> createAccount(Long userId, AccountRequest accountRequest);

    String generateUniqueAccountNumber();

    String generateAccountNumber();

    boolean isAccountNumberExists(String accountNumber);

    void transfer(Long accountId, String accountNumber, int amount, String rqspeNm);

    void deposit(Long accountId, int amount, String rqspeNm);

    SuccessResponse<AccountResponse> accountInfo(Long accountId);

    boolean verifyAccount(String accountNumber);

    SuccessResponse<AccountOwnerNameResponse> findUserNameByAccountNumber(String accountNumber);
}
