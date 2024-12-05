package withbeetravel.controller.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import withbeetravel.dto.request.settlementRequestLog.SettlementRequestLogDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.security.UserAuthorizationUtil;
import withbeetravel.service.notification.NotificationService;
import withbeetravel.service.notification.SettlementRequestLogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class SettlementRequestLogController {

    private final NotificationService notificationService;
    private final SettlementRequestLogService settlementRequestLogService;

    @GetMapping
    SuccessResponse<List<SettlementRequestLogDto>> getNotifications() {
        Long userId = UserAuthorizationUtil.getLoginUserId();
        List<SettlementRequestLogDto> settlementRequestLogs = settlementRequestLogService.getSettlementRequestLogs(userId);
        return SuccessResponse.of(HttpStatus.OK.value(), "알림창 조회 성공", settlementRequestLogs);
    }

    /**
     * streamNotifications : 사용자의 요청을 받아 SSE 구독하도록 연결
     * - SseEmitter를 반환
     */
    @GetMapping(value = "/stream", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> streamNotifications(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "")
                                          String lastEventId) {
        Long userId = UserAuthorizationUtil.getLoginUserId();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Connection", "keep-alive");
        headers.add("Cache-Control", "no-cache"); // 캐시 방지
        headers.add("X-Accel-Buffering", "no");

        return new ResponseEntity<>(notificationService.subscribe(userId, lastEventId), headers, HttpStatus.OK);
    }
}
