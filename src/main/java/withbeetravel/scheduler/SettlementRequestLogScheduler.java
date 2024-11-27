package withbeetravel.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import withbeetravel.domain.*;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.SettlementErrorCode;
import withbeetravel.repository.SettlementRequestLogRepository;
import withbeetravel.repository.TravelMemberRepository;
import withbeetravel.repository.TravelRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SettlementRequestLogScheduler {

    private final TravelRepository travelRepository;
    private final TravelMemberRepository travelMemberRepository;
    private final SettlementRequestLogRepository settlementRequestLogRepository;

    // 메일 오후 6시에 실행
    @Scheduled(cron = "0 0 18 * * *", zone = "Asia/Seoul")
    public void createSettlementLogsForEndedTravels() {
        try {
            List<Travel> travels = travelRepository.findAllByTravelEndDate(LocalDate.now());
            for (Travel travel : travels) {
                createSettlementRequestLog(travel);
            }
        } catch (Exception e) {
            throw new CustomException(SettlementErrorCode.SCHEDULER_PROCESSING_FAILED);
        }
    }

    private void createSettlementRequestLog(Travel travel) {
        List<TravelMember> travelMembers = travelMemberRepository.findAllByTravelId(travel.getId());
        travelMembers.forEach(travelMember -> {
                    User user = travelMember.getUser();
                    SettlementRequestLog settlementRequestLog = SettlementRequestLog.builder()
                            .travel(travel)
                            .user(user)
                            .logTitle(LogTitle.PAYMENT_REQUEST)
                            .logMessage(LogTitle.PAYMENT_REQUEST.getMessage(travel.getTravelName()))
                            .build();
                    settlementRequestLogRepository.save(settlementRequestLog);
                });

    }
}
