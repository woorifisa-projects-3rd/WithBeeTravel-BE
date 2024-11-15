package withbeetravel.dto.settlement;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ShowMyDetailPaymentResponse {
    private Long id;
    private int paymentAmount;
    private int requestedAmount;
    private String storeName;
    private LocalDateTime paymentDate;

    private ShowMyDetailPaymentResponse(Long id,
                                        int paymentAmount,
                                        int requestedAmount,
                                        String storeName,
                                        LocalDateTime paymentDate) {
        this.id = id;
        this.paymentAmount = paymentAmount;
        this.requestedAmount = requestedAmount;
        this.storeName = storeName;
        this.paymentDate = paymentDate;
    }

    public static ShowMyDetailPaymentResponse of (Long id, int paymentAmount, int requestedAmount, String storeName, LocalDateTime paymentDate) {
        return new ShowMyDetailPaymentResponse(id, paymentAmount, requestedAmount, storeName, paymentDate);
    }
}