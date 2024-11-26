package withbeetravel.dto.response.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ExpirationDto {
    private String expirationDate;

    public static ExpirationDto from (Date expirationDate) {
        return ExpirationDto.builder().expirationDate(expirationDate.toString()).build();
    }
}
