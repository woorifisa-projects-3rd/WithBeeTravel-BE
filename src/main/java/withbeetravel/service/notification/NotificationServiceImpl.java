package withbeetravel.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import withbeetravel.repository.notification.EmitterRepository;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; //1시간
    private final EmitterRepository emitterRepository;

    @Override
    public SseEmitter subscribe(Long userId, String lastEventId) {

        // SseEmitter를 식별하기 위한 고유 id 생성
        String emitterId = makeTimeIncludeId(userId);

        // 현재 클라이언트를 위한 sseEmitter 객체 생성
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        // emitterId를 키로 사용해 sseEmitter를 저장
        emitterRepository.save(emitterId, sseEmitter);

        // sseEmitter 연결이 완료될 경우
        sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        // sseEmitter 연결에 타임아웃이 발생할 경우
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러 방지를 위한 더미 데이터 전송
        String eventId = makeTimeIncludeId(userId);
        sendNotification(sseEmitter, eventId, emitterId, "EventStream 생성 완료. [userId = " + userId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, sseEmitter);
        }

        return sseEmitter;
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter sseEmitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(sseEmitter, entry.getKey(), emitterId, entry.getValue()));
    }

    private void sendNotification(SseEmitter sseEmitter, String eventId, String emitterId, Object data) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
        }
    }

    @NotNull
    private String makeTimeIncludeId(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }
}
