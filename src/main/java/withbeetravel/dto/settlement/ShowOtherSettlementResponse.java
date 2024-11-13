package withbeetravel.dto.settlement;

public class ShowOtherSettlementResponse {
    private String name;
    private int actualBurdenCost;

    public ShowOtherSettlementResponse(String name, int actualBurdenCost) {
        this.name = name;
        this.actualBurdenCost = actualBurdenCost;
    }
}
