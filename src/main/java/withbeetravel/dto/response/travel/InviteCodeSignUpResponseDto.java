package withbeetravel.dto.response.travel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteCodeSignUpResponseDto {
    private Long travelId;

    @Builder
    public InviteCodeSignUpResponseDto(Long travelId) {
        this.travelId = travelId;
    }
}
