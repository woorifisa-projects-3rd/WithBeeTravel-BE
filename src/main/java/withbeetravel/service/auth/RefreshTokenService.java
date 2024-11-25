package withbeetravel.service.auth;

import withbeetravel.dto.response.auth.SignInResponseDto;

public interface RefreshTokenService {

    SignInResponseDto refreshToken(final String refreshToken);
}
