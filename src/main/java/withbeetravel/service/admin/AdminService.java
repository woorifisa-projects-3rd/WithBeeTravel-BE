package withbeetravel.service.admin;

import org.springframework.data.domain.Page;
import withbeetravel.domain.Travel;
import withbeetravel.dto.request.admin.TravelAdminRequest;
import withbeetravel.dto.request.admin.UserRequest;
import withbeetravel.dto.response.admin.DashboardResponse;
import withbeetravel.dto.request.admin.LoginLogRequest;
import withbeetravel.dto.response.admin.LoginLogResponse;
import withbeetravel.dto.response.admin.TravelAdminResponse;
import withbeetravel.dto.response.admin.UserResponse;

public interface AdminService {
    Page<LoginLogResponse> showAllLoginHistories(LoginLogRequest loginLogRequest);

    DashboardResponse getLoginAttempts();

    Page<UserResponse> showUsers(UserRequest userRequest);

    Page<TravelAdminResponse> showTravels(TravelAdminRequest travelAdminRequest);

    TravelAdminResponse convertToTravelAdminResponse(Travel travel);
}
