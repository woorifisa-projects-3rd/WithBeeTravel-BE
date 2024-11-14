package withbeetravel.dto.settlement;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ShowMyDetailPaymentResponse {
    private int paymentAmount;
    private int requestedAmount;
    private String storeName;
    private LocalDateTime paymentDate;

    private ShowMyDetailPaymentResponse(int paymentAmount,
                                        int requestedAmount,
                                        String storeName,
                                        LocalDateTime paymentDate) {
        this.paymentAmount = paymentAmount;
        this.requestedAmount = requestedAmount;
        this.storeName = storeName;
        this.paymentDate = paymentDate;
    }

    public static ShowMyDetailPaymentResponse of (int paymentAmount, int requestedAmount, String storeName, LocalDateTime paymentDate) {
        return new ShowMyDetailPaymentResponse(paymentAmount, requestedAmount, storeName, paymentDate);
    }
}
