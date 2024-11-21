package withbeetravel.dto.response.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import withbeetravel.domain.SharedPayment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Schema(description = "TRAVEL ID에 따른 여행 지출 내역 정보 Response DTO")
public class SharedPaymentResponse {

    @Schema(description = "공동 결제 내역 ID")
    private Long id;

    @Schema(description = "결제 추가한 사람의 프로필 이미지")
    private String adderProfileIcon;

    @Schema(description = "결제 금액")
    private int paymentAmount;

    @Schema(description = "외화 결제 금액")
    private double foreignPaymentAmount;

    @Schema(description = "환율")
    private double exchangeRate;

    @Schema(description = "통화 단위")
    private String unit;

    @Schema(description = "상호명")
    private String storeName;

    @Schema(description = "모든 멤버가 참여했는지 여부")
    private Boolean isAllMemberParticipated;

    @Schema(description = "참여한 여행 멤버 프로필 이미지 목록")
    private List<String> participatingMembers;

    @Schema(description = "수동 추가 여부")
    private Boolean isManuallyAdded;

    @Schema(description = "결제 일시")
    private LocalDateTime paymentDate;

    public static Page<SharedPaymentResponse> of(
            Page<SharedPayment> sharedPayments,
            int totalTravelMembers,
            Map<Long, List<String>> participatingMembersMap
    ) {
        return sharedPayments.map(payment ->
                SharedPaymentResponse.builder()
                        .id(payment.getId())
                        .adderProfileIcon(payment.getAddedByMember().getUser().getProfileImage())
                        .paymentAmount(payment.getPaymentAmount())
                        .foreignPaymentAmount(payment.getForeignPaymentAmount())
                        .exchangeRate(payment.getExchangeRate())
                        .unit(payment.getCurrencyUnit().name())
                        .storeName(payment.getStoreName())
                        .isAllMemberParticipated(payment.getPaymentParticipatedMembers().size() == totalTravelMembers)
                        .participatingMembers(participatingMembersMap.get(payment.getId()))
                        .isManuallyAdded(payment.isManuallyAdded())
                        .paymentDate(payment.getPaymentDate())
                        .build()
        );
    }
}
