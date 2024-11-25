package withbeetravel.service.notification;

import withbeetravel.dto.request.settlementRequestLog.SettlementRequestLogDto;

import java.util.List;

public interface SettlementRequestLogService {
    List<SettlementRequestLogDto> getSettlementRequestLogs (Long userId);
}
