package withbeetravel.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PaymentErrorCode extends ErrorCode{

    public static final PaymentErrorCode SHARED_PAYMENT_NOT_FOUND = new PaymentErrorCode(HttpStatus.NOT_FOUND, "SHARED_PAYMENT_NOT_FOUND", "PAYMENT-001", "SHARED PAYMENT ID에 해당하는 공동 결제 내역이 없음");
    public static final PaymentErrorCode NO_PERMISSION_TO_MODIFY_SHARED_PAYMENT = new PaymentErrorCode(HttpStatus.FORBIDDEN, "NO_PERMISSION_TO_MODIFY_SHARED_PAYMENT", "PAYMENT-002", "해당 공동 결제 내역 수정 권한 없음");

    private PaymentErrorCode(HttpStatus status, String name, String code, String message) {
        super(status, name, code, message);
    }
}
