package withbeetravel.dto.response.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "방문 나라 우선 통화 코드 정보 Response DTO")
public class CurrencyUnitResponse {

    @Schema(
            description = "통화 코드 리스트",
            example = "['KRW','USD']"
    )
    private List<String> currencyUnitOptions;
}
