package withbeetravel.dto.banking.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import withbeetravel.domain.Product;

@Getter
@Setter
@AllArgsConstructor
public class AccountRequest {

    private String accountNumber;
    private long balance;
    private Product product;
    private boolean isConnected;


}
