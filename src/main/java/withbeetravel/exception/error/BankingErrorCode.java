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

    public static final BankingErrorCode HISTORY_ACCESS_FORBIDDEN
            = new BankingErrorCode(HttpStatus.FORBIDDEN,"HISTORY_ACCESS_FORBIDDEN","BANKING-006","해당 거래 내역의 접근 권한이 부족합니다");

    public static final BankingErrorCode PAYMENT_ALREADY_EXISTS
            = new BankingErrorCode(HttpStatus.NOT_FOUND,"PAYMENT_ALREADY_EXISTS","BANKING-007","이미 추가된 결제 내역입니다");

    public static final BankingErrorCode INSUFFICIENT_MANAGER_ACCOUNT_BALANCE
            = new BankingErrorCode(HttpStatus.BAD_REQUEST,"INSUFFICIENT_MANAGER_ACCOUNT_BALANCE","BANKING-008","관리자 계좌의 잔액 부족");

    public BankingErrorCode(HttpStatus status,
                            String name, String code, String message) {
        super(status, name, code, message);
    }
}
