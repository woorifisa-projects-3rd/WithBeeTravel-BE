package withbeetravel.service.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import withbeetravel.domain.User;
import withbeetravel.dto.request.auth.SignUpRequest;
import withbeetravel.jwt.JwtUtil;
import withbeetravel.repository.LoginLogRepository;
import withbeetravel.repository.RefreshTokenRepository;
import withbeetravel.repository.UserRepository;
import withbeetravel.service.loginLog.LoginLogServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock private UserRepository userRepository;
    @Mock private JwtUtil jwtUtil;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private LoginLogRepository loginLogRepository;
    @InjectMocks private AuthServiceImpl authService;
    @Mock private LoginLogServiceImpl loginLogService;

    @Test
    void 회원가입을_할_수_있다() {
        SignUpRequest signUpRequest = new SignUpRequest(
                "1234@naver.com", "password123!", "123456", "공예진");

        given(userRepository.existsByEmail(signUpRequest.getEmail())).willReturn(false);

        authService.signUp(signUpRequest);
        verify(userRepository, times(1)).save(any(User.class));
        verify(loginLogService, times(1))
                .logRegister(any(User.class), eq(signUpRequest.getEmail()));
    }
}