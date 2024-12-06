package withbeetravel.dto.response.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import withbeetravel.domain.SharedPayment;

@Data
@Builder
@Schema(description = "SHARED PAYMENT ID에 따른 여행 기록 정보 Response DTO")
public class SharedPaymentRecordResponse {

    @Schema(
            description = "SHARED PAYMENT ID",
            example = "1234"
    )
    private Long sharedPaymentId;

    @Schema(
            description = "여행 기록 이미지",
            example = "https://~"
    )
    private String paymentImage;

    @Schema(
            description = "여행 기록 문구",
            example = "이얏호우~"
    )
    private String paymentComment;

    @Schema(
            description = "메인 여행 사진 여부",
            example = "true"
    )
    private boolean mainImage;

    public static SharedPaymentRecordResponse from(SharedPayment sharedPayment, boolean isMainImage) {

        return SharedPaymentRecordResponse.builder()
                .sharedPaymentId(sharedPayment.getId())
                .paymentImage(sharedPayment.getPaymentImage())
                .paymentComment(sharedPayment.getPaymentComment())
                .mainImage(isMainImage)
                .build();
    }
}
