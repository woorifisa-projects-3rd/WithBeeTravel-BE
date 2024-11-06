package withbeetravel.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import withbeetravel.exception.error.AuthErrorCode;

@Getter
@AllArgsConstructor
public class AuthException extends RuntimeException {

    AuthErrorCode errorCode;
}
