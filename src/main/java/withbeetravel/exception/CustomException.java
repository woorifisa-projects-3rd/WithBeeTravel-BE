package withbeetravel.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import withbeetravel.exception.error.ErrorCode;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{

    ErrorCode errorCode;
}
