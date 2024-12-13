package withbeetravel.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import withbeetravel.domain.RoleType;

@Getter
@NoArgsConstructor
public class UserAuthResponse {
    private String accessToken;
    private RoleType role;

    @Builder
    public UserAuthResponse(String accessToken, RoleType role) {
        this.accessToken = accessToken;
        this.role = role;
    }

    public static UserAuthResponse of (String accessToken, RoleType role) {
        return UserAuthResponse.builder().accessToken(accessToken).role(role).build();
    }
}
