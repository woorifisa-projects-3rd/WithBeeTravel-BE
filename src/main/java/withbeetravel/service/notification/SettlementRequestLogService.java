package withbeetravel.service.notification;

import withbeetravel.domain.SettlementRequest;
import withbeetravel.dto.request.settlementRequestLog.SettlementRequestLogDto;

import java.util.List;

public interface SettlementRequestLogService {
    List<SettlementRequestLogDto> getSettlementRequestLogs (Long userId);

    void createSettlementLogsForEndedTravels();

    void createSettlementReRequestLogForNotAgreed(SettlementRequest settlementRequest);

}
