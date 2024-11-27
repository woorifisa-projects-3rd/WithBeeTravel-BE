package withbeetravel.dto.response.settlement;

import lombok.Getter;

import java.util.List;

@Getter
public class MyDetailPaymentResponse {
    private final int totalPaymentAmounts;
    private final int totalRequestedAmounts;
    private final List<ShowMyDetailPaymentResponse> myDetailPaymentResponses;

    public MyDetailPaymentResponse(int totalPaymentAmounts, int totalRequestedAmounts, List<ShowMyDetailPaymentResponse> myDetailPaymentResponses) {
        this.totalPaymentAmounts = totalPaymentAmounts;
        this.totalRequestedAmounts = totalRequestedAmounts;
        this.myDetailPaymentResponses = myDetailPaymentResponses;
    }

    public static MyDetailPaymentResponse of (int totalPaymentAmounts, int totalRequestedAmounts, List<ShowMyDetailPaymentResponse> myDetailPaymentResponses) {
        return new MyDetailPaymentResponse(totalPaymentAmounts, totalRequestedAmounts, myDetailPaymentResponses);
    }
}
