package withbeetravel.dto.response.travel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InviteCodeResponseDto {
    private Long travelId;

    @Builder
    public InviteCodeResponseDto(Long travelId) {
        this.travelId = travelId;
    }
}
