package withbeetravel.dto.request.admin;

import lombok.Getter;

@Getter
public class LoginLogRequest {
    private Long userId;
    private int page;
    private int size;

    public LoginLogRequest(Long userId, int page, int size) {
        this.userId = userId;
        this.page = page;
        this.size = size;
    }
}
