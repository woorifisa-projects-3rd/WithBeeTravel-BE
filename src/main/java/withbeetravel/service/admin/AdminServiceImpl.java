package withbeetravel.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import withbeetravel.domain.LoginLog;
import withbeetravel.domain.LoginLogType;
import withbeetravel.dto.response.admin.DashboardResponse;
import withbeetravel.dto.request.admin.LoginLogRequest;
import withbeetravel.dto.response.admin.LoginLogResponse;
import withbeetravel.repository.LoginLogRepository;
import withbeetravel.repository.TravelRepository;
import withbeetravel.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final LoginLogRepository loginLogRepository;
    private final UserRepository userRepository;
    private final TravelRepository travelRepository;

    public Page<LoginLogResponse> showAllLoginHistories(LoginLogRequest loginLogRequest){

        Pageable pageable = PageRequest.of(loginLogRequest.getPage(), loginLogRequest.getSize());

        Page<LoginLog> loginLogs;

        if (loginLogRequest.getLoginLogType() != null) {
            loginLogs = loginLogRepository.findAllByUser_IdAndLoginLogType(loginLogRequest.getUserId(),
                    loginLogRequest.getLoginLogType(), pageable);
        } else {
            loginLogs = loginLogRepository.findAllByUser_Id(loginLogRequest.getUserId(), pageable);
        }

        return loginLogs.map(LoginLogResponse::from);
    }

    public DashboardResponse getLoginAttempts() {
        List<LoginLogType> loginLogTypes = Arrays.asList(LoginLogType.LOGIN, LoginLogType.LOGIN_FAILED);

        return DashboardResponse.builder().loginCount(loginLogRepository.countByLoginLogTypeIn(loginLogTypes))
                .totalUser(userRepository.count())
                .totalTravel(travelRepository.count()).build();
    }


}
