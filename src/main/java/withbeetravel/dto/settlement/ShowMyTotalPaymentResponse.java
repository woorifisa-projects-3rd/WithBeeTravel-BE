package withbeetravel.dto.settlement;

import lombok.Getter;

@Getter
public class ShowMyTotalPaymentResponse {
    private int totalPaymentCost;
    private int ownPaymentCost;
    private int actualBurdenCost;

    private ShowMyTotalPaymentResponse(int totalPaymentCost, int ownPaymentCost, int actualBurdenCost) {
        this.totalPaymentCost = totalPaymentCost;
        this.ownPaymentCost = ownPaymentCost;
        this.actualBurdenCost = actualBurdenCost;
    }

    public static ShowMyTotalPaymentResponse of (int totalPaymentCost, int ownPaymentCost, int actualBurdenCost) {
        return new ShowMyTotalPaymentResponse(totalPaymentCost, ownPaymentCost, actualBurdenCost);
    }
}