package withbeetravel.service.notification;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe(Long userId, String lastEventId);
}
