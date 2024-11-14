package withbeetravel.dto.settlement;

public class ShowMyTotalPaymentResponse {
    private int totalPaymentCost;
    private int ownPaymentCost;
    private int actualBurdenCost;

    private ShowMyTotalPaymentResponse(int totalPaymentCost, int ownPaymentCost, int actualBurdenCost) {
        this.totalPaymentCost = totalPaymentCost;
        this.ownPaymentCost = ownPaymentCost;
        this.actualBurdenCost = actualBurdenCost;
    }
}