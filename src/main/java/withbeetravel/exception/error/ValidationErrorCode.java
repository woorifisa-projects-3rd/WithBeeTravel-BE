package withbeetravel.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidationErrorCode extends ErrorCode{

    public static final ValidationErrorCode MISSING_REQUIRED_FIELDS = new ValidationErrorCode(HttpStatus.BAD_REQUEST, "MISSING_REQUIRED_FIELDS", "VALIDATION-001", "필수 정보 누락");
    public static final ValidationErrorCode INVALID_DATE_FORMAT = new ValidationErrorCode(HttpStatus.BAD_REQUEST, "INVALID_DATE_FORMAT", "VALIDATION-002", "유효하지 않은 날짜 형식");
    public static final ValidationErrorCode DATE_RANGE_ERROR = new ValidationErrorCode(HttpStatus.BAD_REQUEST, "DATE_RANGE_ERROR", "VALIDATION-003", "날짜 범위 오류");
    public static final ValidationErrorCode IMAGE_PROCESSING_FAILED = new ValidationErrorCode(HttpStatus.UNPROCESSABLE_ENTITY, "IMAGE_PROCESSING_FAILED", "VALIDATION-004", "이미지 처리 중 오류가 발생했습니다.");
    public static final ValidationErrorCode INVALID_CURRENCY_UNIT = new ValidationErrorCode(HttpStatus.BAD_REQUEST, "INVALID_CURRENCY_UNIT", "VALIDATION-005", "지원되지 않는 통화 코드입니다.");

    private ValidationErrorCode(HttpStatus status, String name, String code, String message) {
        super(status, name, code, message);
    }
}
