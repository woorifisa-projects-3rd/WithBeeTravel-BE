package withbeetravel.service.loginLog;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.LoginLog;
import withbeetravel.domain.LoginLogType;
import withbeetravel.domain.User;
import withbeetravel.repository.LoginLogRepository;

@Service
@RequiredArgsConstructor
@EnableAsync
public class LoginLogServiceImpl implements LoginLogService {
    private final LoginLogRepository loginLogRepository;

    @Async
    public void logRegister(User user, String email){
        LoginLog registerLoginLog = LoginLog.builder()
                .loginLogType(LoginLogType.REGISTER)
                .user(user)
                .description("회원 가입 완료")
                .ipAddress(email)
                .build();
        loginLogRepository.save(registerLoginLog);
    }

    @Async
    public void logLoginSuccess(User user, String email) {
        LoginLog loginLog = LoginLog.builder()
                .loginLogType(LoginLogType.LOGIN)
                .user(user)
                .description("로그인 성공")
                .ipAddress(email)
                .build();
        loginLogRepository.save(loginLog);
    }

    @Async
    @Transactional
    public void logLoginFailed(User user, String email) {
        LoginLog loginFailedLoginLog = LoginLog.builder()
                .loginLogType(LoginLogType.LOGIN_FAILED)
                .user(user)
                .description("로그인 실패 - 잘못된 비밀번호")
                .ipAddress(email) // 이메일을 IP로 사용하거나 실제 IP를 받아서 사용 가능
                .build();
        loginLogRepository.save(loginFailedLoginLog);
    }
}
