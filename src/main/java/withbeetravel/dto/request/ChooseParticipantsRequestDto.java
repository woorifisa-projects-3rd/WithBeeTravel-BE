package withbeetravel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "공동 결제 내역에 대한 정산 인원 리스트 DTO")
public class ChooseParticipantsRequestDto {

    @Schema(
            description = "정산 인원 리스트",
            example = "[17, 19, 22, 27]"
    )
    List<Long> travelMembersId;
}
