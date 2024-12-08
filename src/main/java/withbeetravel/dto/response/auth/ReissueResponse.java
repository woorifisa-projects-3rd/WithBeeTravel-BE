package withbeetravel.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReissueResponse {
    private AccessTokenResponse accessTokenResponse;
    private String refreshToken;

    @Builder
    public ReissueResponse(AccessTokenResponse accessTokenResponse, String refreshToken) {
        this.accessTokenResponse = accessTokenResponse;
        this.refreshToken = refreshToken;
    }

    public static ReissueResponse of (AccessTokenResponse accessTokenResponse, String refreshToken) {
        return ReissueResponse.builder().accessTokenResponse(accessTokenResponse).refreshToken(refreshToken).build();
    }
}