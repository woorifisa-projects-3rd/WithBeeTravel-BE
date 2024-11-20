package withbeetravel.dto.request.travel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InviteCodeSignUpRequestDto {

    private String inviteCode;

    public InviteCodeSignUpRequestDto(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
