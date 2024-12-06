package withbeetravel.service.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import withbeetravel.domain.Account;
import withbeetravel.domain.User;
import withbeetravel.dto.response.auth.MyPageResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.repository.UserRepository;
import withbeetravel.support.AccountFixture;
import withbeetravel.support.UserFixture;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
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
}