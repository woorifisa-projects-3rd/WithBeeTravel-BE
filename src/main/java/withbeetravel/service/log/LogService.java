package withbeetravel.service.log;

import withbeetravel.domain.User;

public interface LogService {
    
    void logLoginSuccess(User user, String email);

    void logLoginFailed(User user, String email);
}
