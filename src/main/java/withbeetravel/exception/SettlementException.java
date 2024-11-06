package withbeetravel.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import withbeetravel.exception.error.SettlementErrorCode;

@Getter
@AllArgsConstructor
public class SettlementException extends RuntimeException {

    SettlementErrorCode errorCode;
}