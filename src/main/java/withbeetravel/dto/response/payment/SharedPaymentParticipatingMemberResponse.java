package withbeetravel.dto.response.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SharedPaymentParticipatingMemberResponse {
    @Schema(description = "여행 멤버 ID")
    private Long id;

    @Schema(description = "프로필 이미지")
    private int profileImage;
}