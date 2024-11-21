package withbeetravel.service.banking;

import withbeetravel.dto.request.account.CreateAccountRequest;
import withbeetravel.dto.response.account.AccountConnectedWibeeResponse;
import withbeetravel.dto.response.account.AccountOwnerNameResponse;
import withbeetravel.dto.request.account.AccountRequest;
import withbeetravel.dto.response.account.AccountResponse;
import withbeetravel.dto.response.SuccessResponse;

import java.util.List;

public interface AccountService {

    List<AccountResponse> showAll(Long userId);

    AccountResponse createAccount(Long userId, CreateAccountRequest CreateAccountRequest);

    String generateUniqueAccountNumber();

    String generateAccountNumber();

    boolean isAccountNumberExists(String accountNumber);

    void transfer(Long accountId, String accountNumber, int amount, String rqspeNm);

    void deposit(Long accountId, int amount, String rqspeNm);

    AccountResponse accountInfo(Long accountId);

    void verifyAccount(String accountNumber);

    AccountOwnerNameResponse findUserNameByAccountNumber(String accountNumber);

    AccountConnectedWibeeResponse connectedWibee(Long accountId);
}
