package withbeetravel.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import withbeetravel.exception.*;
import withbeetravel.exception.dto.ErrorResponseDto;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AuthException.class)
    protected ResponseEntity<ErrorResponseDto> handleAuthException(AuthException e) {
        return ErrorResponseDto.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(PaymentException.class)
    protected ResponseEntity<ErrorResponseDto> handlePaymentException(PaymentException e) {
        return ErrorResponseDto.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(SettlementException.class)
    protected ResponseEntity<ErrorResponseDto> handleSettlementException(SettlementException e) {
        return ErrorResponseDto.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(TravelException.class)
    protected ResponseEntity<ErrorResponseDto> handleTravelException(TravelException e) {
        return ErrorResponseDto.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ErrorResponseDto> handleValidationException(ValidationException e) {
        return ErrorResponseDto.toResponseEntity(e.getErrorCode());
    }
}