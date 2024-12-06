package withbeetravel.dto.response.admin;

import lombok.Builder;
import lombok.Data;
import withbeetravel.domain.LoginLog;
import withbeetravel.domain.User;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long userId;
    private String userEmail;
    private String userName;
    private boolean userPinLocked;
    private String userRoleType;
    private String createAt;
    private String recentLogin;

    public static UserResponse from(User user, LoginLog register, LoginLog loginLog){
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.isPinLocked(),
                user.getRoleType().toString(),
                register.getCreatedAt().toString(),
                loginLog.getCreatedAt().toString()
        );
    }
}
