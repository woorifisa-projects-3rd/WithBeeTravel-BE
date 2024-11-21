package withbeetravel.controller.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import withbeetravel.dto.request.settlementRequestLog.SettlementRequestLogDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.notification.SettlementRequestLogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class SettlementRequestLogController {

    private final SettlementRequestLogService settlementRequestLogService;

    private final Long userId = 1L;
    
    @GetMapping
    SuccessResponse<List<SettlementRequestLogDto>> getNotifications() {
        List<SettlementRequestLogDto> settlementRequestLogs = settlementRequestLogService.getSettlementRequestLogs(userId);
        return SuccessResponse.of(HttpStatus.OK.value(), "알림창 조회 성공", settlementRequestLogs);
    }
}
