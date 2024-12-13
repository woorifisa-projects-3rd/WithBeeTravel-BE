package withbeetravel.dto.response.travel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import withbeetravel.domain.SharedPayment;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "허니캡슐 Response DTO")
public class HoneyCapsuleResponse {

    @Schema(
            description = "SHARED PAYMENT ID",
            example = "1234"
    )
    private Long sharedPaymentId;

    @Schema(
            description = "결제 일자",
            example = "2024-11-05T14:13:00"
    )
    private LocalDateTime paymentDate;

    @Schema(
            description = "기록 사진(URL)",
            example = "https://~"
    )
    private String paymentImage;

    @Schema(
            description = "기록 문구",
            example = "포케 파라다이스 \uD83D\uDD25\uD83D\uDD25"
    )
    private String paymentComment;

    @Schema(
            description = "상호명",
            example = "Poke Bowls"
    )
    private String storeName;

    @Schema(
            description = "원화 결제 금액(nullable)",
            example = "95000"
    )
    private Integer paymentAmount;

    @Schema(
            description = "외화 결제 금액",
            example = "95000.0"
    )
    private Double foreignPaymentAmount;

    @Schema(
            description = "화폐 단위",
            example = "USD"
    )
    private String unit;

    @Schema(
            description = "결제 내역 추가 멤버의 프로필 이미지",
            example = "1"
    )
    private int addMemberProfileImage;

    public static HoneyCapsuleResponse from(SharedPayment sharedPayment) {

        return HoneyCapsuleResponse.builder()
                .sharedPaymentId(sharedPayment.getId())
                .paymentDate(sharedPayment.getPaymentDate())
                .paymentImage(sharedPayment.getPaymentImage())
                .paymentComment(sharedPayment.getPaymentComment())
                .storeName(sharedPayment.getStoreName())
                .paymentAmount(sharedPayment.getPaymentAmount())
                .foreignPaymentAmount(sharedPayment.getForeignPaymentAmount())
                .unit(sharedPayment.getCurrencyUnit().name())
                .addMemberProfileImage(sharedPayment.getAddedByMember().getUser().getProfileImage())
                .build();
    }
}
