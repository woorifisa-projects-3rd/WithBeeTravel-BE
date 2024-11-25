package withbeetravel.dto.response.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponseDto {
    private String accessToken;
    private String refreshToken;

    @Builder
    public SignInResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static SignInResponseDto of (String accessToken, String refreshToken) {
        return SignInResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
}
