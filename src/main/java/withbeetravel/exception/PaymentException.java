package withbeetravel.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import withbeetravel.exception.error.PaymentErrorCode;

@Getter
@AllArgsConstructor
public class PaymentException extends RuntimeException {

    PaymentErrorCode errorCode;
}
