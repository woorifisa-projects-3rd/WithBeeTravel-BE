package withbeetravel.service.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import withbeetravel.domain.Account;
import withbeetravel.domain.User;
import withbeetravel.dto.request.auth.SignUpRequest;
import withbeetravel.dto.response.auth.MyPageResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.jwt.JwtUtil;
import withbeetravel.repository.LoginLogRepository;
import withbeetravel.repository.RefreshTokenRepository;
import withbeetravel.repository.UserRepository;
import withbeetravel.service.loginLog.LoginLogServiceImpl;
import withbeetravel.support.AccountFixture;
import withbeetravel.support.UserFixture;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private LoginLogRepository loginLogRepository;
    @Mock private LoginLogServiceImpl loginLogService;
    @InjectMocks private AuthServiceImpl authService;

    @Test
    void 사용자_정보를_정상적으로_가져올_수_있다() {
        User user = UserFixture.builder().id(1L).build();
        Account account = AccountFixture.builder().user(user).isConnectedWibeeCard(true).build();

        user.updateConnectedAccount(account);
        user.updateWibeeCardAccount(account);

        // given
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        // when
        MyPageResponse response = authService.getMyPageInfo(user.getId());

        // then
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(1, response.getProfileImage()),
                () -> assertEquals("홍길동", response.getUsername()),
                () -> assertEquals("WON통장", response.getAccountProduct()),
                () -> assertEquals("1111111111111", response.getAccountNumber())
        );
    }

    @Test
    void 존재하지_않는_사용자_ID에_대한_예외를_발생시킨다() {
        // given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> authService.getMyPageInfo(userId));
        assertEquals(AuthErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

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