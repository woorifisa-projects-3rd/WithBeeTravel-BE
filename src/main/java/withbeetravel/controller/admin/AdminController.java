package withbeetravel.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.dto.request.admin.TravelAdminRequest;
import withbeetravel.dto.request.admin.UserRequest;
import withbeetravel.dto.response.admin.DashboardResponse;
import withbeetravel.dto.request.admin.LoginLogRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.admin.LoginLogResponse;
import withbeetravel.dto.response.admin.TravelAdminResponse;
import withbeetravel.dto.response.admin.UserResponse;
import withbeetravel.service.admin.AdminService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public SuccessResponse<DashboardResponse> showDashboard(){
        return SuccessResponse.of(
                HttpStatus.OK.value(),
                "로그인, 유저, 여행 수 조회 성공",
                adminService.getLoginAttempts()
        );
    }

    @GetMapping("/logs/all")
    public SuccessResponse<Page<LoginLogResponse>> showAllLoginHistories(
            @RequestParam Long userId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String loginLogType
            ){
        LoginLogRequest loginLogRequest = new LoginLogRequest(userId, page-1, size,loginLogType);
        return SuccessResponse.of(
                HttpStatus.OK.value(),
                "userId의 전체 로그인 로그 조회 성공",
                adminService.showAllLoginHistories(loginLogRequest)
        );
    }

    @GetMapping("/users")
    public SuccessResponse<Page<UserResponse>> showUsers(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String name
    ) {
        UserRequest userRequest = UserRequest.builder().name(name).
                page(page).size(size).build();
        return SuccessResponse.of(
                HttpStatus.OK.value(),
                "사용자 목록 조회 성공",
                adminService.showUsers(userRequest)
        );
    }

    @GetMapping("/travels")
    public SuccessResponse<Page<TravelAdminResponse>> showTravels(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) Long userId
    ){
        TravelAdminRequest travelAdminRequest = TravelAdminRequest.builder()
                .page(page).size(size).userId(userId).build();

        return SuccessResponse.of(
                HttpStatus.OK.value(),
                userId == null ? "전체 여행 조회 성공" : "사용자의 여행 조회 성공",
                adminService.showTravels(travelAdminRequest)
        );

    }


}
