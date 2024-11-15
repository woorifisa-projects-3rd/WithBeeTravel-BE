package withbeetravel.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "허니캡슐 리스트 Response DTO")
public class HoneyCapsuleListResponse {

    @Schema(
            description = "허니캡슐 정보 리스트"
    )
    private List<HoneyCapsuleResponse> honeyCapsuleList;
}
