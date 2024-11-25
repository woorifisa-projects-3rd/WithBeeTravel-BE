package withbeetravel.dto.response.settlement;

import lombok.Getter;

import java.util.List;

@Getter
public class ShowSettlementDetailResponse {
    private ShowMyTotalPaymentResponse myTotalPayment;
    private final int disagreeCount;
    private List<ShowMyDetailPaymentResponse> myDetailPayments;
    private List<ShowOtherSettlementResponse> others;

    private ShowSettlementDetailResponse(
            ShowMyTotalPaymentResponse myTotalPayment,
            int disagreeCount,
            List<ShowMyDetailPaymentResponse> myDetailPayments,
            List<ShowOtherSettlementResponse> others) {
        this.myTotalPayment = myTotalPayment;
        this.disagreeCount = disagreeCount;
        this.myDetailPayments = myDetailPayments;
        this.others = others;
    }

    public static ShowSettlementDetailResponse of (
            ShowMyTotalPaymentResponse myTotalPayment,
            int disagreeCount,
            List<ShowMyDetailPaymentResponse> myDetailPayments,
            List<ShowOtherSettlementResponse> others) {
        return new ShowSettlementDetailResponse(myTotalPayment, disagreeCount, myDetailPayments, others);
    }
}