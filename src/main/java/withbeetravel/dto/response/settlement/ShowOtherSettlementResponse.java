package withbeetravel.dto.response.settlement;

import lombok.Getter;

@Getter
public class ShowOtherSettlementResponse {
    private Long id;
    private String name;
    private int totalPaymentCost;
    private boolean isAgreed;

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