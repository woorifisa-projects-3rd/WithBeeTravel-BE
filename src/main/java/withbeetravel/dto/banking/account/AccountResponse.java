package withbeetravel.dto.banking.account;

import lombok.Getter;
import withbeetravel.domain.Account;
import withbeetravel.domain.Product;

@Getter
public class AccountResponse {

    private Long accountId;
    private String accountNumber;
    private long balance;
    private Product product;

    public AccountResponse(Long accountId,String accountNumber, long balance, Product product){
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.product = product;
    }

    public static AccountResponse from(Account account){
        return new AccountResponse(account.getId(), account.getAccountNumber(), account.getBalance(), account.getProduct());
    }

}
