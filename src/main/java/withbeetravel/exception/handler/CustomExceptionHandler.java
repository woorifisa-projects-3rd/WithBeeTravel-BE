package withbeetravel.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.dto.ErrorResponseDto;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseDto> handleAuthException(CustomException e) {
        return ErrorResponseDto.toResponseEntity(e.getErrorCode());
    }
}