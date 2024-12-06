package withbeetravel.dto.response.admin;

import lombok.Getter;
import withbeetravel.domain.LoginLog;

@Getter
public class LoginLogResponse {

    private Long logId;
    private String createdAt;
    private String description;
    private String ipAddress;
    private String logType;
    private Long userId;

    // 기본 생성자
    public LoginLogResponse(Long logId, String createdAt, String description, String ipAddress, String logType, Long userId) {
        this.logId = logId;
        this.createdAt = createdAt;
        this.description = description;
        this.ipAddress = ipAddress;
        this.logType = logType;
        this.userId = userId;
    }

    public static LoginLogResponse of(Long logId, String createdAt, String description, String ipAddress, String logType, Long userId) {
        return new LoginLogResponse(logId, createdAt, description, ipAddress, logType, userId);
    }

    public static LoginLogResponse from(LoginLog loginLog) {
        return new LoginLogResponse(
                loginLog.getId(),
                loginLog.getCreatedAt().toString(),
                loginLog.getDescription(),
                loginLog.getIpAddress(),
                loginLog.getLoginLogType().toString(),
                loginLog.getUser().getId()
        );
    }

}
