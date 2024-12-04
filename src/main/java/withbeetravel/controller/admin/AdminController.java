package withbeetravel.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.dto.response.admin.DashboardResponse;
import withbeetravel.dto.request.admin.LoginLogRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.admin.LoginLogResponse;
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


}
