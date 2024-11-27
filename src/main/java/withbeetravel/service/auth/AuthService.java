package withbeetravel.service.auth;

import withbeetravel.dto.request.auth.SignInRequestDto;
import withbeetravel.dto.request.auth.SignUpRequestDto;
import withbeetravel.dto.response.auth.ExpirationDto;
import withbeetravel.dto.response.auth.SignInResponseDto;

public interface AuthService {
    void signUp(SignUpRequestDto signUpRequestDto);
    SignInResponseDto login(SignInRequestDto signInRequestDto);

    SignInResponseDto refreshToken(final String refreshToken);

    ExpirationDto checkExpirationTime(final String refreshToken);
}
