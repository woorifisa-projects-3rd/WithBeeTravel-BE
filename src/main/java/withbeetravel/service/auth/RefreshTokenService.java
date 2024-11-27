package withbeetravel.service.auth;

import withbeetravel.dto.request.auth.RefreshTokenDto;
import withbeetravel.dto.response.auth.ExpirationDto;
import withbeetravel.dto.response.auth.SignInResponseDto;

public interface RefreshTokenService {

    SignInResponseDto refreshToken(final String refreshToken);

    ExpirationDto checkExpirationTime(RefreshTokenDto refreshTokenDto);
}
