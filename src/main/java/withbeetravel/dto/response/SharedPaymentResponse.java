package withbeetravel.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;
import withbeetravel.domain.SharedPayment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@Schema(description = "TRAVEL ID에 따른 여행 지출 내역 정보 Response DTO")
public class SharedPaymentResponse {

    private Long sharedPaymentId;
    private String adderProfileIcon;
    private int paymentAmount;
    private double foreignPaymentAmount;
    private double exchangeRate;
    private String unit;
    private String storeName;
    private Boolean isAllMemberParticipated;
    private List<Long> travelMembers;
    private Boolean isManuallyAdded;
    private LocalDateTime paymentDate;

    public static SharedPaymentResponse from(SharedPayment sharedPayment) {
        // Travel의 전체 멤버 수를 가져오는 로직
        int totalTravelMembers = sharedPayment.getTravel().getTravelMembers().size();

        // 해당 지출에 참여한 멤버들의 ID를 가져오는 로직
        List<Long> participatingMembers = sharedPayment.getPaymentParticipatedMembers().stream()
                .map(member -> member.getTravelMember().getId())
                .collect(Collectors.toList());

        return SharedPaymentResponse.builder()
                .sharedPaymentId(sharedPayment.getId())
                .adderProfileIcon(sharedPayment.getPaymentImage())
                .paymentAmount(sharedPayment.getPaymentAmount())
                .foreignPaymentAmount(sharedPayment.getForeignPaymentAmount())
                .exchangeRate(sharedPayment.getExchangeRate())
                .unit(sharedPayment.getCurrencyUnit().name())
                .storeName(sharedPayment.getStoreName())
                .isAllMemberParticipated(sharedPayment.getPaymentParticipatedMembers().size() == totalTravelMembers)
                .travelMembers(participatingMembers)
                .isManuallyAdded(sharedPayment.isManuallyAdded())
                .paymentDate(sharedPayment.getPaymentDate())
                .build();
    }
}
