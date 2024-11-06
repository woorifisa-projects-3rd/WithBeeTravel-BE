package withbeetravel.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import withbeetravel.exception.AuthException;
import withbeetravel.exception.ErrorResponseDto;

@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(AuthException.class)
    protected ResponseEntity<ErrorResponseDto> handleAuthException(AuthException e) {
        return ErrorResponseDto.toResponseEntity(e.getErrorCode());
    }
}
