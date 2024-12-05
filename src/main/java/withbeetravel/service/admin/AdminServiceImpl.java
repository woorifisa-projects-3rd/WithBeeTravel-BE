package withbeetravel.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import withbeetravel.domain.*;
import withbeetravel.dto.request.admin.TravelAdminRequest;
import withbeetravel.dto.request.admin.UserRequest;
import withbeetravel.dto.response.admin.DashboardResponse;
import withbeetravel.dto.request.admin.LoginLogRequest;
import withbeetravel.dto.response.admin.LoginLogResponse;
import withbeetravel.dto.response.admin.TravelAdminResponse;
import withbeetravel.dto.response.admin.UserResponse;
import withbeetravel.repository.LoginLogRepository;
import withbeetravel.repository.TravelMemberRepository;
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
    private final TravelMemberRepository travelMemberRepository;

    public DashboardResponse getLoginAttempts() {
        List<LoginLogType> loginLogTypes = Arrays.asList(LoginLogType.LOGIN, LoginLogType.LOGIN_FAILED);

        return DashboardResponse.builder().loginCount(loginLogRepository.countByLoginLogTypeIn(loginLogTypes))
                .totalUser(userRepository.count())
                .totalTravel(travelRepository.count()).build();
    }

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

    public Page<UserResponse> showUsers(UserRequest userRequest) {
        Pageable pageable = PageRequest.of(userRequest.getPage() - 1, userRequest.getSize());
        Page<User> users;

        if (userRequest.getName() != null && !userRequest.getName().isEmpty()) {
            users = userRepository.findByNameContaining(userRequest.getName(), pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.map(user -> {
            LoginLog registerLog = loginLogRepository.findFirstRegisterLogByUserId(user.getId())
                    .orElse(null);
            LoginLog recentLoginLog = loginLogRepository.findMostRecentLoginLogByUserId(user.getId())
                    .orElse(null);
            return UserResponse.from(user, registerLog, recentLoginLog);
        });
    }

    public Page<TravelAdminResponse> showTravels(TravelAdminRequest travelAdminRequest) {
        Pageable pageable = PageRequest.of(travelAdminRequest.getPage() - 1, travelAdminRequest.getSize());

        if (travelAdminRequest.getUserId() == null) {
            return travelRepository.findAll(pageable)
                    .map(this::convertToTravelAdminResponse);
        } else {
            return travelMemberRepository.findAllByUserId(travelAdminRequest.getUserId(), pageable)
                    .map(travelMember -> convertToTravelAdminResponse(travelMember.getTravel()));
        }
    }

    public TravelAdminResponse convertToTravelAdminResponse(Travel travel) {
        return TravelAdminResponse.builder()
                .travelId(travel.getId())
                .travelName(travel.getTravelName())
                .travelType(travel.isDomesticTravel() ? "극내" : "해외")
                .travelStartDate(travel.getTravelStartDate().toString())
                .travelEndDate(travel.getTravelEndDate().toString())
                .totalMember(travel.getTravelMembers().size())
                .captainId(travel.getTravelMembers().stream()
                        .filter(TravelMember::isCaptain)
                        .findFirst()
                        .map(travelMember -> travelMember.getUser().getId())
                        .orElse(null))
                .settlementStatus(travel.getSettlementStatus().toString())
                .build();
    }



}
