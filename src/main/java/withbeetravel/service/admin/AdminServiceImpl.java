package withbeetravel.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import withbeetravel.domain.LoginLog;
import withbeetravel.dto.request.admin.LoginLogRequest;
import withbeetravel.dto.response.admin.LoginLogResponse;
import withbeetravel.repository.LoginLogRepository;
import withbeetravel.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final LoginLogRepository loginLogRepository;
    private final UserRepository userRepository;

    public Page<LoginLogResponse> showAllLoginHistories(LoginLogRequest loginLogRequest){

        Pageable pageable = PageRequest.of(loginLogRequest.getPage(), loginLogRequest.getSize());

        Page<LoginLog> loginLogs = loginLogRepository.findAllByUser_Id(loginLogRequest.getUserId(), pageable);

        Page<LoginLogResponse> response = loginLogs.map(loginLog -> LoginLogResponse.from(loginLog));

        return response;
    }

}
