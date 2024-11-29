package withbeetravel.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import withbeetravel.controller.notification.NotificationController;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.SettlementErrorCode;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    @Override
    public SseEmitter subscribe(Long userId) {

        // 1. 현재 클라이언트를 위한 sseEmitter 객체 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        // 2. 연결
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            throw new CustomException(SettlementErrorCode.SSE_CONNECTION_FAILED);
        }

        // 3. 저장
        NotificationController.sseEmitters.put(userId, sseEmitter);

        // 4. 연결 종료 처리
        // sseEmitter 연결이 완료될 경우
        sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(userId));
        // sseEmitter 연결에 타임아웃이 발생할 경우
        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(userId));
        // sseEmitter 연결에 오류가 발생할 경우
        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(userId));

        return sseEmitter;
    }
}
