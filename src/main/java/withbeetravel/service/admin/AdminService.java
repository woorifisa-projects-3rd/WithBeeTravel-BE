package withbeetravel.service.admin;

import org.springframework.data.domain.Page;
import withbeetravel.dto.response.admin.DashboardResponse;
import withbeetravel.dto.request.admin.LoginLogRequest;
import withbeetravel.dto.response.admin.LoginLogResponse;

public interface AdminService {
    Page<LoginLogResponse> showAllLoginHistories(LoginLogRequest loginLogRequest);

    DashboardResponse getLoginAttempts();
}
