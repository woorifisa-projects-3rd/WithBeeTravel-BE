package withbeetravel.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BankingErrorCode extends ErrorCode{

    public static final BankingErrorCode INSUFFICIENT_FUNDS
            = new BankingErrorCode(HttpStatus.BAD_REQUEST,"INSUFFICIENT_FUNDS","BANKING-001","계좌 잔액 부족");

    public static final BankingErrorCode ACCOUNT_NOT_FOUND
            = new BankingErrorCode(HttpStatus.NOT_FOUND,"ACCOUNT_NOT_FOUND","BANKING-002","존재하지 않는 계좌번호");

    public static final BankingErrorCode TRANSFER_LIMIT_EXCEEDED
            = new BankingErrorCode(HttpStatus.BAD_REQUEST,"TRANSFER_LIMIT_EXCEEDED","BANKING-003","하루 이체 한도 초과");

    public static final BankingErrorCode INSUFFICIENT_MANAGER_ACCOUNT_BALANCE
            = new BankingErrorCode(HttpStatus.BAD_REQUEST,"INSUFFICIENT_MANAGER_ACCOUNT_BALANCE","BANKING-008","관리자 계좌의 잔액 부족");



    public BankingErrorCode(HttpStatus status,
                            String name, String code, String message) {
        super(status, name, code, message);
    }
}
