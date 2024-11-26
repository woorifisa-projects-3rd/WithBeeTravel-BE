package withbeetravel.dto.response.settlement;

import lombok.Getter;

@Getter
public class ShowMyTotalPaymentResponse {
    private String name;
    private boolean isAgreed;
    private int totalPaymentCost;
    private int ownPaymentCost;
    private int actualBurdenCost;

    private ShowMyTotalPaymentResponse(String name, boolean isAgreed, int totalPaymentCost, int ownPaymentCost, int actualBurdenCost) {
        this.name = name;
        this.isAgreed = isAgreed;
        this.totalPaymentCost = totalPaymentCost;
        this.ownPaymentCost = ownPaymentCost;
        this.actualBurdenCost = actualBurdenCost;
    }

    public static ShowMyTotalPaymentResponse of (String name, boolean isAgreed, int ownPaymentCost, int actualBurdenCost) {
        return new ShowMyTotalPaymentResponse(name, isAgreed, ownPaymentCost - actualBurdenCost, ownPaymentCost, actualBurdenCost);
    }
}