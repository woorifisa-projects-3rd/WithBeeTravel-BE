package withbeetravel.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import withbeetravel.domain.LogTitle;
import withbeetravel.domain.SettlementRequestLog;
import withbeetravel.domain.Travel;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.SettlementErrorCode;
import withbeetravel.repository.SettlementRequestLogRepository;
import withbeetravel.repository.TravelRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SettlementRequestLogScheduler {

    private final TravelRepository travelRepository;
    private final SettlementRequestLogRepository settlementRequestLogRepository;

    // 메일 오후 6시에 실행
    @Scheduled(cron = "0 0 18 * * *", zone = "Asia/Seoul")
    public void createSettlementLogsForEndedTravels() {
        try {
            List<Travel> travels = travelRepository.findAllByTravelEndDate(LocalDate.now());
            for (Travel travel : travels) {
                SettlementRequestLog settlementRequestLog = createSettlementRequestLog(travel);
                settlementRequestLogRepository.save(settlementRequestLog);
            }
        } catch (Exception e) {
            throw new CustomException(SettlementErrorCode.SCHEDULER_PROCESSING_FAILED);
        }
    }

    private SettlementRequestLog createSettlementRequestLog(Travel travel) {
        return SettlementRequestLog.builder()
                .travel(travel)
                .logTitle(LogTitle.PAYMENT_REQUEST)
                .logMessage(LogTitle.PAYMENT_REQUEST.getMessage(travel.getTravelName()))
                .build();
    }
}
