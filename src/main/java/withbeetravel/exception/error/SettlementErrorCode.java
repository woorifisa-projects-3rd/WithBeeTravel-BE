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
    public static final SettlementErrorCode SETTLEMENT_NOT_ONGOING = new SettlementErrorCode(HttpStatus.FORBIDDEN, "SETTLEMENT_NOT_ONGOING", "SETTLEMENT-008", "진행 중인 정산 요청이 아님");
    public static final SettlementErrorCode SETTLEMENT_DISAGREE_COUNT_NOT_CERTAIN = new SettlementErrorCode(HttpStatus.CONFLICT, "SETTLEMENT_DISAGREE_COUNT_NOT_CERTAIN", "SETTLEMENT-009", "이미 모든 정산원이 동의한 정산 요청");
    public static final SettlementErrorCode SETTLEMENT_INSUFFICIENT_BALANCE = new SettlementErrorCode(HttpStatus.CONFLICT, "SETTLEMENT_INSUFFICIENT_BALANCE", "SETTLEMENT-010", "여행멤버의 잔액 부족으로 인한 정산 처리 불가");


    private SettlementErrorCode(HttpStatus status, String name, String code, String message) {
        super(status, name, code, message);
    }
}