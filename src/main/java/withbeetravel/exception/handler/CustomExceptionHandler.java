package withbeetravel.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import withbeetravel.exception.AuthException;
import withbeetravel.exception.PaymentException;
import withbeetravel.exception.SettlementException;
import withbeetravel.exception.TravelException;
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
}
