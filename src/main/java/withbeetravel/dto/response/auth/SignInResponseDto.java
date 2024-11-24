package withbeetravel.dto.response.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponseDto {
    private String accessToken;

    @Builder
    public SignInResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public static SignInResponseDto from (String accessToken) {
        return SignInResponseDto.builder().accessToken(accessToken).build();
    }
}
