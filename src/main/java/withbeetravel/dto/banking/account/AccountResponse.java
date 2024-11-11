package withbeetravel.dto.banking.account;

import lombok.Getter;
import withbeetravel.domain.Account;
import withbeetravel.domain.Product;

@Getter
public class AccountResponse {

    private String accountNumber;
    private long balance;
    private Product product;

    public AccountResponse(String accountNumber, long balance, Product product){
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.product = product;
    }

    public static AccountResponse from(Account account){
        return new AccountResponse(account.getAccountNumber(), account.getBalance(), account.getProduct());
    }

}
