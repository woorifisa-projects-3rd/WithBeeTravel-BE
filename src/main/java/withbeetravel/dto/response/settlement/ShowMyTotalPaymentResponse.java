package withbeetravel.dto.response.settlement;

import lombok.Getter;

@Getter
public class ShowMyTotalPaymentResponse {
    private final String name;
    private final boolean isAgreed;
    private final int totalPaymentCost;

    private ShowMyTotalPaymentResponse(String name, boolean isAgreed, int totalPaymentCost) {
        this.name = name;
        this.isAgreed = isAgreed;
        this.totalPaymentCost = totalPaymentCost;
    }

    public static ShowMyTotalPaymentResponse of (String name, boolean isAgreed, int ownPaymentCost, int actualBurdenCost) {
        return new ShowMyTotalPaymentResponse(name, isAgreed, ownPaymentCost - actualBurdenCost);
    }
}