package withbeetravel.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.SettlementErrorCode;

import withbeetravel.service.notification.SettlementRequestLogService;

@Component
@RequiredArgsConstructor
public class SettlementRequestLogScheduler {

    private final SettlementRequestLogService settlementRequestLogService;

    // 메일 오후 6시에 실행
//    @Scheduled(cron = "0 0 18 * * *", zone = "Asia/Seoul")
    @Scheduled(cron = "0 35 11 * * *", zone = "Asia/Seoul")
    public void createSettlementLogsForEndedTravels() {
        try {
            settlementRequestLogService.createSettlementLogsForEndedTravels();
        } catch (Exception e) {
            throw new CustomException(SettlementErrorCode.SCHEDULER_PROCESSING_FAILED);
        }
    }
}
