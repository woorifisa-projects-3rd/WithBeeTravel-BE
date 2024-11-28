package withbeetravel.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccessTokenDto {
    private String accessToken;

    @Builder
    public AccessTokenDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
