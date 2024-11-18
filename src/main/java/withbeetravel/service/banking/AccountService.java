package withbeetravel.service.banking;

import withbeetravel.dto.response.account.AccountOwnerNameResponse;
import withbeetravel.dto.request.account.AccountRequest;
import withbeetravel.dto.response.account.AccountResponse;
import withbeetravel.dto.response.SuccessResponse;

import java.util.List;

public interface AccountService {

    SuccessResponse<List<AccountResponse>> showAll(Long userId);

    SuccessResponse<AccountResponse> createAccount(Long userId, AccountRequest accountRequest);

    String generateUniqueAccountNumber();

    String generateAccountNumber();

    boolean isAccountNumberExists(String accountNumber);

    void transfer(Long accountId, String accountNumber, int amount, String rqspeNm);

    void deposit(Long accountId, int amount, String rqspeNm);

    SuccessResponse<AccountResponse> accountInfo(Long accountId);

    SuccessResponse verifyAccount(String accountNumber);

    SuccessResponse<AccountOwnerNameResponse> findUserNameByAccountNumber(String accountNumber);
}
