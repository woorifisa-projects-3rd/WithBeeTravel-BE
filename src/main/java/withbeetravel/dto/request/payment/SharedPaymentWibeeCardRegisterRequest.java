package withbeetravel.dto.request.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "위비 카드 결제 내역 공동 결제 내역에 추가 Request DTO")
public class SharedPaymentWibeeCardRegisterRequest {

    @Schema(
            description = "위비 카드 연동 계좌의 계좌 내역 Id",
            example = "[17, 19, 22, 27]"
    )
    List<Long> historyId;
}
