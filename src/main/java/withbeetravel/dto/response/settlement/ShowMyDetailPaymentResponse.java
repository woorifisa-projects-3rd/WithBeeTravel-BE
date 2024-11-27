package withbeetravel.dto.response.settlement;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ShowMyDetailPaymentResponse {
    private final Long id;
    private final int paymentAmount;
    private final int requestedAmount;
    private final String storeName;
    private final LocalDateTime paymentDate;

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