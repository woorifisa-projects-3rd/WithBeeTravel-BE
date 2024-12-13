package withbeetravel.dto.response.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import withbeetravel.domain.History;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "위비 카드 결제 내역 정보 Response DTO")
public class WibeeCardHistoryResponse {

    @Schema(description = "결제 내역 ID")
    private Long id;

    @Schema(description = "결제 시간")
    private LocalDateTime date;

    @Schema(description = "결제 금액")
    private int paymentAmount;

    @Schema(description = "상호명")
    private String storeName;

    @Schema(description = "공동 결제 내역 추가 여부")
    private boolean isAddedSharedPayment;

    public static WibeeCardHistoryResponse from (History history) {
        return WibeeCardHistoryResponse.builder()
                .id(history.getId())
                .date(history.getDate())
                .paymentAmount(history.getPayAM())
                .storeName(history.getRqspeNm())
                .isAddedSharedPayment(history.isAddedSharedPayment())
                .build();
    }
}
