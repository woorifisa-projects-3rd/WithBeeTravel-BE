package withbeetravel.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInResponseDto {
    private AccessTokenDto accessTokenDto;
    private String refreshToken;

    @Builder
    public SignInResponseDto(AccessTokenDto accessTokenDto, String refreshToken) {
        this.accessTokenDto = accessTokenDto;
        this.refreshToken = refreshToken;
    }

    public static SignInResponseDto of (AccessTokenDto accessTokenDto, String refreshToken) {
        return SignInResponseDto.builder().accessTokenDto(accessTokenDto).refreshToken(refreshToken).build();
    }
}
