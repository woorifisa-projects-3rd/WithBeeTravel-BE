package withbeetravel.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInResponse {
    private AccessTokenResponse accessTokenResponse;
    private String refreshToken;

    @Builder
    public SignInResponse(AccessTokenResponse accessTokenResponse, String refreshToken) {
        this.accessTokenResponse = accessTokenResponse;
        this.refreshToken = refreshToken;
    }

    public static SignInResponse of (AccessTokenResponse accessTokenResponse, String refreshToken) {
        return SignInResponse.builder().accessTokenResponse(accessTokenResponse).refreshToken(refreshToken).build();
    }
}
