package withbeetravel.dto.request.admin;

import lombok.Getter;
import withbeetravel.domain.LoginLogType;

@Getter
public class LoginLogRequest {
    private Long userId;
    private int page;
    private int size;
    private String loginLogType;

    public LoginLogRequest(Long userId, int page, int size, String loginLogType) {
        this.userId = userId;
        this.page = page;
        this.size = size;
        this.loginLogType = loginLogType;
    }

    public LoginLogType getLoginLogType() {
        if (loginLogType != null) {
            return LoginLogType.valueOf(loginLogType.toUpperCase()); // String을 Enum으로 변환
        }
        return null;
    }
}
