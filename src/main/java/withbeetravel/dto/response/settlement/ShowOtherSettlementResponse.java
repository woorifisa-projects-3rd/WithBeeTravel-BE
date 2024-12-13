package withbeetravel.dto.response.settlement;

import lombok.Getter;

@Getter
public class ShowOtherSettlementResponse {
    private final Long id;
    private final String name;
    private final int totalPaymentCost;
    private final boolean isAgreed;

    private ShowOtherSettlementResponse(Long id, String name, int totalPaymentCost, boolean isAgreed) {
        this.id = id;
        this.name = name;
        this.totalPaymentCost = totalPaymentCost;
        this.isAgreed = isAgreed;
    }

    public static ShowOtherSettlementResponse of (Long id, String name, int totalPaymentCost, boolean isAgreed) {
        return new ShowOtherSettlementResponse(id, name, totalPaymentCost, isAgreed);
    }
}