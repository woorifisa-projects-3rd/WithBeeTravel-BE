package withbeetravel.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import withbeetravel.exception.error.TravelErrorCode;

@Getter
@AllArgsConstructor
public class TravelException extends RuntimeException {

    TravelErrorCode errorCode;
}
