package withbeetravel.dto.response.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "위비 카드 결제 내역 리스트 Response DTO")
public class WibeeCardHistoryListResponse {

    @Schema(description = "결제 내역 시작 범위")
    private String startDate;

    @Schema(description = "결제 내역 끝 범위")
    private String endDate;

    @Schema(description = "결제 내역 리스트")
    private List<WibeeCardHistoryResponse> histories;
}
