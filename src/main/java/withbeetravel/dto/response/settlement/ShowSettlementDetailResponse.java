package withbeetravel.dto.response.settlement;

import lombok.Getter;

import java.util.List;

@Getter
public class ShowSettlementDetailResponse {
    private final ShowMyTotalPaymentResponse myTotalPayment;
    private final int disagreeCount;
    private final int totalPaymentAmounts;
    private final int totalRequestedAmounts;
    private final List<ShowMyDetailPaymentResponse> myDetailPayments;
    private final List<ShowOtherSettlementResponse> others;

    private ShowSettlementDetailResponse(
            ShowMyTotalPaymentResponse myTotalPayment,
            int disagreeCount,
            int totalPaymentAmounts,
            int totalRequestedAmounts,
            List<ShowMyDetailPaymentResponse> myDetailPayments,
            List<ShowOtherSettlementResponse> others) {
        this.myTotalPayment = myTotalPayment;
        this.disagreeCount = disagreeCount;
        this.totalPaymentAmounts = totalPaymentAmounts;
        this.totalRequestedAmounts = totalRequestedAmounts;
        this.myDetailPayments = myDetailPayments;
        this.others = others;
    }

    public static ShowSettlementDetailResponse of (
            ShowMyTotalPaymentResponse myTotalPayment,
            int disagreeCount,
            int totalPaymentAmounts,
            int totalRequestedAmounts,
            List<ShowMyDetailPaymentResponse> myDetailPayments,
            List<ShowOtherSettlementResponse> others) {
        return new ShowSettlementDetailResponse(
                myTotalPayment, disagreeCount, totalPaymentAmounts, totalRequestedAmounts, myDetailPayments, others);
    }
}