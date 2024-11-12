package withbeetravel.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BankingErrorCode extends ErrorCode{

    public static final BankingErrorCode INSUFFICIENT_FUNDS_ERROR
            = new BankingErrorCode(HttpStatus.BAD_REQUEST,"INSUFFICIENT_FUNDS_ERROR","BANKING-001","계좌 잔액 부족");

    public static final BankingErrorCode ACCOUNT_NOT_FOUND_ERROR
            = new BankingErrorCode(HttpStatus.NOT_FOUND,"ACCOUNT_NOT_FOUND_ERROR","BANKING-002","존재하지 않는 계좌번호");

    public static final BankingErrorCode TRANSFER_LIMIT_EXCEEDED_ERROR
            = new BankingErrorCode(HttpStatus.BAD_REQUEST,"TRANSFER_LIMIT_EXCEEDED_ERROR","BANKING-003","하루 이체 한도 초과");



    public BankingErrorCode(HttpStatus status,
                            String name, String code, String message) {
        super(status, name, code, message);
    }
}
