package withbeetravel.dto.settlement;

import java.time.LocalDateTime;

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
}