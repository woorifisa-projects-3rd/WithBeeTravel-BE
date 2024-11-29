package withbeetravel.dto.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "계좌 내역 Request DTO")
public class HistoryRequest {

    @Schema(
            description = "결제 금액",
            example = "8900"
    )
    private Integer payAm;

    @Schema(
            description = "상호명",
            example = "Tokyo Banana"
    )
    private String rqspeNm;

    @Schema(
            description = "위비카드로 결제 여부",
            example = "true/false"
    )
    @JsonProperty("isWibeeCard")
    private boolean isWibeeCard;
}
