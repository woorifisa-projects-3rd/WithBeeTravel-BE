package withbeetravel.dto.response.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import withbeetravel.domain.SharedPayment;
import withbeetravel.domain.TravelMember;
import withbeetravel.dto.response.travel.TravelMemberResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@Schema(description = "TRAVEL ID에 따른 여행 지출 내역 정보 리스트 Response DTO")
public class SharedPaymentListResponse {

    @Schema(description = "여행 멤버 목록")
    private List<TravelMemberResponse> travelMembers;

    @Schema(description = "결제 내역 목록")
    private Page<SharedPaymentResponse> payments;

    public static SharedPaymentListResponse of(
            Page<SharedPayment> sharedPayments,
            List<TravelMember> travelMembers,
            int totalTravelMembers,
            Map<Long, List<String>> participatingMembersMap
    ) {
        return SharedPaymentListResponse.builder()
                .travelMembers(travelMembers.stream()
                        .map(TravelMemberResponse::from)
                        .collect(Collectors.toList()))
                .payments(SharedPaymentResponse.of(
                        sharedPayments,
                        totalTravelMembers,
                        participatingMembersMap
                ))
                .build();
    }
}
