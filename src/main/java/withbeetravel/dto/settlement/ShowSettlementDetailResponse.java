package withbeetravel.dto.settlement;

import java.util.List;

public class ShowSettlementDetailResponse {
    private ShowMyTotalPaymentResponse myTotalPayment;
    private List<ShowMyDetailPaymentResponse> myDetailPayments;
    private List<ShowOtherSettlementResponse> others;

    private ShowSettlementDetailResponse(
            ShowMyTotalPaymentResponse myTotalPayment,
            List<ShowMyDetailPaymentResponse> myDetailPayments,
            List<ShowOtherSettlementResponse> others) {
        this.myTotalPayment = myTotalPayment;
        this.myDetailPayments = myDetailPayments;
        this.others = others;
    }

    public static ShowSettlementDetailResponse of (
            ShowMyTotalPaymentResponse myTotalPayment,
            List<ShowMyDetailPaymentResponse> myDetailPayments,
            List<ShowOtherSettlementResponse> others) {
        return new ShowSettlementDetailResponse(myTotalPayment, myDetailPayments, others);
    }
}
