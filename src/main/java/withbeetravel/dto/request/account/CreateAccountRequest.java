package withbeetravel.dto.request.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import withbeetravel.domain.Product;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    private Product product;
}
