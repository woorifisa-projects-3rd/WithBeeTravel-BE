package withbeetravel.controller.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import withbeetravel.security.UserAuthorizationUtil;
import withbeetravel.service.notification.NotificationService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    // 모든 Emitters를 저장하는 ConcurrentHashMap
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    /**
     * streamNotifications : 사용자의 요청을 받아 SSE 구독하도록 연결
     * - SseEmitter를 반환
     */
    @GetMapping("/stream")
    public SseEmitter streamNotifications() {
        Long userId = UserAuthorizationUtil.getLoginUserId();
        return notificationService.subscribe(userId);
    }
}
