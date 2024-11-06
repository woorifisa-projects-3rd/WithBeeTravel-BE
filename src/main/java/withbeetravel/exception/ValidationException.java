package withbeetravel.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import withbeetravel.exception.error.ValidationErrorCode;

@Getter
@AllArgsConstructor
public class ValidationException extends RuntimeException {

    ValidationErrorCode errorCode;
}
