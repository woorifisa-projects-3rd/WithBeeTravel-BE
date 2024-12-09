package withbeetravel.dto.request.account;

import lombok.Builder;
import lombok.Getter;
import withbeetravel.domain.Product;

@Getter
@Builder
public class CreateAccountRequest {
    private Product product;
}
