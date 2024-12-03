package withbeetravel.service.log;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.Log;
import withbeetravel.domain.LogType;
import withbeetravel.domain.User;
import withbeetravel.repository.LogRepository;

@Service
@RequiredArgsConstructor
@EnableAsync
public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;

    @Async
    public void logLoginSuccess(User user, String email) {
        Log loginLog = Log.builder()
                .logType(LogType.LOGIN)
                .user(user)
                .description("로그인 성공")
                .ipAddress(email)
                .build();
        logRepository.save(loginLog);
    }

    @Async
    @Transactional
    public void logLoginFailed(User user, String email) {
        System.out.println("로그 저장 시도");
        Log loginFailedLog = Log.builder()
                .logType(LogType.LOGIN_FAILED)
                .user(user)
                .description("로그인 실패 - 잘못된 비밀번호")
                .ipAddress(email) // 이메일을 IP로 사용하거나 실제 IP를 받아서 사용 가능
                .build();
        logRepository.save(loginFailedLog);
    }


}
