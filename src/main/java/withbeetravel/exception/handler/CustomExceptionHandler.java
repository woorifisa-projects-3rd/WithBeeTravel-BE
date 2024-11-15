package withbeetravel.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import withbeetravel.exception.CustomException;
import withbeetravel.dto.response.ErrorResponse;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleAuthException(CustomException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}