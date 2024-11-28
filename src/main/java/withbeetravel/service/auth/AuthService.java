package withbeetravel.service.auth;

import withbeetravel.dto.request.auth.SignInRequest;
import withbeetravel.dto.request.auth.SignUpRequest;
import withbeetravel.dto.response.auth.ExpirationResponse;
import withbeetravel.dto.response.auth.SignInResponse;

public interface AuthService {
    void signUp(SignUpRequest signUpRequest);
    SignInResponse login(SignInRequest signInRequest);

    SignInResponse reissue(final String refreshToken);

    ExpirationResponse checkExpirationTime(final String refreshToken);

    void logout(final String refreshToken);
}
