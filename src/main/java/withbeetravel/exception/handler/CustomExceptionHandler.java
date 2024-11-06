package withbeetravel.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import withbeetravel.exception.AuthException;
import withbeetravel.exception.dto.ErrorResponseDto;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AuthException.class)
    protected ResponseEntity<ErrorResponseDto> handleAuthException(AuthException e) {
        System.out.println("custom exception handler");
        return ErrorResponseDto.toResponseEntity(e.getErrorCode());
    }
}
