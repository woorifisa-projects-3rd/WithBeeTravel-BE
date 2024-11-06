package withbeetravel.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode {

    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH-001", "사용자 인증 실패"),
    ADMIN_PRIVILEGES_REQUIRED(HttpStatus.FORBIDDEN, "AUTH-002", "관리자 권한 없음"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH-003", "잘못된 이메일 또는 비밀번호"),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH-004", "존재하지 않는 이메일"),
    TERMS_NOT_ACCEPTED(HttpStatus.FORBIDDEN, "AUTH-005", "서비스 이용 약관에 동의하지 않음"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "AUTH-006", "이미 존재하는 이메일"),
    PASSWORD_POLICY_VIOLATION(HttpStatus.BAD_REQUEST, "AUTH-007", "비밀번호 정책 불충족"),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "AUTH-008", "잘못된 이메일 형식"),
    PIN_POLICY_VIOLATION(HttpStatus.BAD_REQUEST, "AUTH-009", "핀번호 정책 불충족");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
