package withbeetravel.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PaymentErrorCode extends ErrorCode{

    public static final PaymentErrorCode SHARED_PAYMENT_NOT_FOUND = new PaymentErrorCode(HttpStatus.NOT_FOUND, "SHARED_PAYMENT_NOT_FOUND", "PAYMENT-001", "SHARED PAYMENT ID에 해당하는 공동 결제 내역이 없음");
    public static final PaymentErrorCode NO_PERMISSION_TO_MODIFY_SHARED_PAYMENT = new PaymentErrorCode(HttpStatus.FORBIDDEN, "NO_PERMISSION_TO_MODIFY_SHARED_PAYMENT", "PAYMENT-002", "해당 공동 결제 내역 수정 권한 없음");
    public static final PaymentErrorCode SHARED_PAYMENT_ACCESS_FORBIDDEN = new PaymentErrorCode(HttpStatus.FORBIDDEN, "SHARED_PAYMENT_ACCESS_FORBIDDEN", "PAYMENT-003", "해당 공동 결제 내역 정보 접근 권한 없음");
    public static final PaymentErrorCode NON_TRAVEL_MEMBER_INCLUDED = new PaymentErrorCode(HttpStatus.FORBIDDEN, "NON_TRAVEL_MEMBER_INCLUDED", "PAYMENT-004", "여행 멤버가 아닌 Travel Member ID가 포함되어 있음");

    private PaymentErrorCode(HttpStatus status, String name, String code, String message) {
        super(status, name, code, message);
    }
}
