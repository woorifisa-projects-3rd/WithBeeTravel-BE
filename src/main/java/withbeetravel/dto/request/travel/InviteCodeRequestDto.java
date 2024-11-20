package withbeetravel.dto.request.travel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InviteCodeRequestDto {

    private String inviteCode;

    public InviteCodeRequestDto(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
