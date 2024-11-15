package withbeetravel.dto.request.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "공동 결제 내역 참여 멤버 수정 Request DTO")
public class SharedPaymentParticipateRequest {

    @Schema(
            description = "공동 결제 내역 참여 멤버 리스트",
            example = "[17, 19, 22, 27]"
    )
    private List<Long> travelMembersId;
}
