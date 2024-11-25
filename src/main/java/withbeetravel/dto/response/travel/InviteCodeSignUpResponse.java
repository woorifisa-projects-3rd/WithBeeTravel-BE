package withbeetravel.dto.response.travel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteCodeSignUpResponse {
    private Long travelId;

    @Builder
    public InviteCodeSignUpResponse(Long travelId) {
        this.travelId = travelId;
    }
}
