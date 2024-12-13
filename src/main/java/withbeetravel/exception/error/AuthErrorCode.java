package withbeetravel.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthErrorCode extends ErrorCode{

    public static final AuthErrorCode AUTHENTICATION_FAILED = new AuthErrorCode(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_FAILED", "AUTH-001", "사용자 인증 실패");
    public static final AuthErrorCode ADMIN_PRIVILEGES_REQUIRED = new AuthErrorCode(HttpStatus.FORBIDDEN, "ADMIN_PRIVILEGES_REQUIRED", "AUTH-002", "관리자 권한 없음");
    public static final AuthErrorCode INVALID_CREDENTIALS = new AuthErrorCode(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "AUTH-003", "잘못된 이메일 또는 비밀번호");
    public static final AuthErrorCode EMAIL_NOT_FOUND = new AuthErrorCode(HttpStatus.NOT_FOUND, "EMAIL_NOT_FOUND", "AUTH-004", "존재하지 않는 이메일");
    public static final AuthErrorCode TERMS_NOT_ACCEPTED = new AuthErrorCode(HttpStatus.FORBIDDEN, "TERMS_NOT_ACCEPTED", "AUTH-005", "서비스 이용 약관에 동의하지 않음");
    public static final AuthErrorCode EMAIL_ALREADY_EXISTS = new AuthErrorCode(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS", "AUTH-006", "이미 존재하는 이메일");
    public static final AuthErrorCode PASSWORD_POLICY_VIOLATION = new AuthErrorCode(HttpStatus.BAD_REQUEST, "AUTH-007", "PASSWORD_POLICY_VIOLATION", "비밀번호 정책 불충족");
    public static final AuthErrorCode INVALID_EMAIL_FORMAT = new AuthErrorCode(HttpStatus.BAD_REQUEST, "AUTH-008", "INVALID_EMAIL_FORMAT", "잘못된 이메일 형식");
    public static final AuthErrorCode PIN_POLICY_VIOLATION = new AuthErrorCode(HttpStatus.BAD_REQUEST, "PIN_POLICY_VIOLATION", "AUTH-009", "핀번호 정책 불충족");
    public static final AuthErrorCode INVALID_PASSWORD = new AuthErrorCode(HttpStatus.BAD_REQUEST, "INVALID_PASSWORD", "AUTH-010", "잘못된 비밀번호");
    public static final AuthErrorCode INVALID_JWT = new AuthErrorCode(HttpStatus.UNAUTHORIZED, "INVALID_JWT", "AUTH-011", "잘못된 JWT 토큰");
    public static final AuthErrorCode EXPIRED_JWT = new AuthErrorCode(HttpStatus.UNAUTHORIZED, "EXPIRED_JWT", "AUTH-012", "만료된 JWT 토큰");
    public static final AuthErrorCode UNSUPPORTED_JWT = new AuthErrorCode(HttpStatus.UNAUTHORIZED, "UNSUPPORTED_JWT", "AUTH-013", "지원되지 않는 JWT 토큰");
    public static final AuthErrorCode EMPTY_JWT = new AuthErrorCode(HttpStatus.BAD_REQUEST, "EMPTY_JWT", "AUTH-014", "빈 JWT 토큰");
    public static final AuthErrorCode REFRESH_TOKEN_NOT_FOUND = new AuthErrorCode(HttpStatus.NOT_FOUND, "REFRESH_TOKEN_NOT_FOUND", "AUTH-015", "리프레시 토큰 없음");
    public static final AuthErrorCode INVALID_REFRESH_TOKEN = new AuthErrorCode(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "AUTH-016", "잘못된 리프레시 토큰");
    public static final AuthErrorCode USER_NOT_FOUND = new AuthErrorCode(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "AUTH-017", "사용자를 찾을 수 없음");
    public static final AuthErrorCode VALIDATION_FAILED = new AuthErrorCode(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "AUTH-018", "유효성 검사 실패");
    public static final AuthErrorCode SCHEDULER_PROCESSING_FAILED = new AuthErrorCode(HttpStatus.UNPROCESSABLE_ENTITY, "SCHEDULER_PROCESSING_FAILED", "AUTH-019", "리프레시 토큰 삭제 스케줄링 중 오류가 발생했습니다.");
    public static final AuthErrorCode ACCOUNT_NOT_CREATED = new AuthErrorCode(HttpStatus.UNPROCESSABLE_ENTITY, "ACCOUNT_NOT_CREATED", "AUTH-020", "회원가입 중 계좌 생성 오류");

    private AuthErrorCode(HttpStatus status, String name, String code, String message) {
        super(status, name, code, message);
    }
}
