package withbeetravel.dto.request.travel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteCodeRequestDto {

    private String inviteCode;

    public InviteCodeRequestDto(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
