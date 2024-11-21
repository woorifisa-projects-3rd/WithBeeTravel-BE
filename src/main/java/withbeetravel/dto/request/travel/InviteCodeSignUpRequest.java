package withbeetravel.dto.request.travel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InviteCodeSignUpRequest {

    private String inviteCode;

    public InviteCodeSignUpRequest(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
