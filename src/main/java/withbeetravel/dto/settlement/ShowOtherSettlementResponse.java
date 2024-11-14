package withbeetravel.dto.settlement;

public class ShowOtherSettlementResponse {
    private String name;
    private int totalPaymentCost;

    private ShowOtherSettlementResponse(String name, int totalPaymentCost) {
        this.name = name;
        this.totalPaymentCost = totalPaymentCost;
    }

    public static ShowOtherSettlementResponse of (String name, int totalPaymentCost) {
        return new ShowOtherSettlementResponse(name, totalPaymentCost);
    }
}