package withbeetravel.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInResponse {
    private UserAuthResponse userAuthResponse;
    private String refreshToken;

    @Builder
    public SignInResponse(UserAuthResponse userAuthResponse, String refreshToken) {
        this.userAuthResponse = userAuthResponse;
        this.refreshToken = refreshToken;
    }

    public static SignInResponse of (UserAuthResponse userAuthResponse, String refreshToken) {
        return SignInResponse.builder().userAuthResponse(userAuthResponse).refreshToken(refreshToken).build();
    }
}

