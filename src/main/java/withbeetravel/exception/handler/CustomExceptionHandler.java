package withbeetravel.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import withbeetravel.exception.CustomException;
import withbeetravel.dto.response.ErrorResponse;
import withbeetravel.exception.error.AuthErrorCode;


@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleAuthException(CustomException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        // 유효성 검사에서 실패한 필드와 오류 메시지를 추출
        String errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                .orElse("유효성 검사 실패");

        // ErrorResponse 객체를 생성하여 응답 반환
        AuthErrorCode validationFailed = AuthErrorCode.VALIDATION_FAILED;
        return ResponseEntity.status(validationFailed.getStatus())
                .body(ErrorResponse.builder()
                        .status(validationFailed.getStatus().value())
                        .name(validationFailed.getName())
                        .code(validationFailed.getCode())
                        .message(errorMessages)
                        .build());
    }
}