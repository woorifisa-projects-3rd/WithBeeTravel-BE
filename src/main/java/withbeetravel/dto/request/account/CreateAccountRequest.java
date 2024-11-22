package withbeetravel.dto.request.account;

import lombok.Getter;
import withbeetravel.domain.Product;

@Getter
public class CreateAccountRequest {
    private Product product;
}
