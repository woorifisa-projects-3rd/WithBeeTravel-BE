package withbeetravel.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SettlementErrorCode extends ErrorCode{

    public static final SettlementErrorCode SETTLEMENT_NOT_FOUND = new SettlementErrorCode(HttpStatus.NOT_FOUND, "SETTLEMENT_NOT_FOUND", "SETTLEMENT-002", "SETTLEMENT ID에 해당하는 정산 요청 정보 없음");
    public static final SettlementErrorCode SETTLEMENT_ALREADY_AGREED = new SettlementErrorCode(HttpStatus.CONFLICT, "SETTLEMENT_ALREADY_AGREED", "SETTLEMENT-003", "이미 동의한 정산");
    public static final SettlementErrorCode SETTLEMENT_NOT_COMPLETED = new SettlementErrorCode(HttpStatus.CONFLICT, "SETTLEMENT_NOT_COMPLETED", "SETTLEMENT-005", "아직 정산 완료 되지 않음");
    public static final SettlementErrorCode NO_PERMISSION_TO_MANAGE_SETTLEMENT = new SettlementErrorCode(HttpStatus.FORBIDDEN, "NO_PERMISSION_TO_MANAGE_SETTLEMENT", "SETTLEMENT-006", "정산 관리 권한 없음");
    public static final SettlementErrorCode MEMBER_SETTLEMENT_HISTORY_NOT_FOUND = new SettlementErrorCode(HttpStatus.NOT_FOUND, "MEMBER_SETTLEMENT_HISTORY_NOT_FOUND", "SETTLEMENT-007", "TRAVELMEMBER ID에 해당하는 여행 멤버 정산 내역 없음");

    private SettlementErrorCode(HttpStatus status, String name, String code, String message) {
        super(status, name, code, message);
    }
}
