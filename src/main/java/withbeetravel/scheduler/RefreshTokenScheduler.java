package withbeetravel.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.service.auth.AuthService;

@Component
@RequiredArgsConstructor
public class RefreshTokenScheduler {

    private final AuthService authService;

    @Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Seoul")
    public void deleteExpiredToken() {
        try {
            authService.deleteExpiredToken();
        } catch (Exception e) {
            throw new CustomException(AuthErrorCode.SCHEDULER_PROCESSING_FAILED);
        }
    }

}
