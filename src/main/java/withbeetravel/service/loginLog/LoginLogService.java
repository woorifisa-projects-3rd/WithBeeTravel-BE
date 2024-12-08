package withbeetravel.service.loginLog;

import withbeetravel.domain.User;

public interface LoginLogService {

    void logRegister(User user, String email);

    void logLoginSuccess(User user, String email);

    void logLoginFailed(User user, String email);
}
