package withbeetravel.dto.banking.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import withbeetravel.domain.Product;

@Getter
@AllArgsConstructor
@Builder
public class AccountRequest {

    private String accountNumber;
    private long balance;
    private Product product;
    private boolean isConnected;


}
