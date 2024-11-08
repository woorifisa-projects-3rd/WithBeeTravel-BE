package withbeetravel.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public abstract class ErrorCode {

    private final HttpStatus status;
    private final String name;
    private final String code;
    private final String message;
}
