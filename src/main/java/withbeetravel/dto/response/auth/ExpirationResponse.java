package withbeetravel.dto.response.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ExpirationResponse {
    private String expirationDate;

    public static ExpirationResponse from (Date expirationDate) {
        return ExpirationResponse.builder().expirationDate(expirationDate.toString()).build();
    }
}
