package withbeetravel.dto.response.settlement;

import lombok.Getter;

@Getter
public class ShowMyTotalPaymentResponse {
    private String name;
    private int totalPaymentCost;
    private int ownPaymentCost;
    private int actualBurdenCost;

    private ShowMyTotalPaymentResponse(String name, int totalPaymentCost, int ownPaymentCost, int actualBurdenCost) {
        this.name = name;
        this.totalPaymentCost = totalPaymentCost;
        this.ownPaymentCost = ownPaymentCost;
        this.actualBurdenCost = actualBurdenCost;
    }

    public static ShowMyTotalPaymentResponse of (String name, int ownPaymentCost, int actualBurdenCost) {
        return new ShowMyTotalPaymentResponse(name, ownPaymentCost - actualBurdenCost, ownPaymentCost, actualBurdenCost);
    }
}