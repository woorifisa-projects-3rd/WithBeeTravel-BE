package withbeetravel.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserErrorCode extends ErrorCode {

    public static final UserErrorCode USER_NOT_FOUND = new UserErrorCode(
            HttpStatus.NOT_FOUND,
            "USER_NOT_FOUND",
            "USER-001",
            "사용자를 찾을 수 없습니다");

    public static final UserErrorCode USER_EMAIL_DUPLICATE = new UserErrorCode(
            HttpStatus.CONFLICT,
            "USER_EMAIL_DUPLICATE",
            "USER-002",
            "이미 존재하는 이메일입니다");

    public static final UserErrorCode USER_PASSWORD_INVALID = new UserErrorCode(
            HttpStatus.UNAUTHORIZED,
            "USER_PASSWORD_INVALID",
            "USER-003",
            "비밀번호가 일치하지 않습니다");

    public static final UserErrorCode USER_PIN_INVALID = new UserErrorCode(
            HttpStatus.UNAUTHORIZED,
            "USER_PIN_INVALID",
            "USER-004",
            "PIN 번호가 일치하지 않습니다");

    public static final UserErrorCode USER_ACCOUNT_LOCKED = new UserErrorCode(
            HttpStatus.FORBIDDEN,
            "USER_ACCOUNT_LOCKED",
            "USER-005",
            "계정이 잠겨있습니다");

    public static final UserErrorCode USER_NO_ACCOUNT = new UserErrorCode(
            HttpStatus.NOT_FOUND,
            "USER_NO_ACCOUNT",
            "USER-006",
            "연결된 계좌가 없습니다");

    public static final UserErrorCode USER_UNAUTHORIZED = new UserErrorCode(
            HttpStatus.UNAUTHORIZED,
            "USER_UNAUTHORIZED",
            "USER-007",
            "인증되지 않은 사용자입니다");

    public static final UserErrorCode USER_ACCESS_DENIED = new UserErrorCode(
            HttpStatus.FORBIDDEN,
            "USER_ACCESS_DENIED",
            "USER-008",
            "접근 권한이 없습니다");

    private UserErrorCode(HttpStatus status, String name, String code, String message) {
        super(status, name, code, message);
    }
}