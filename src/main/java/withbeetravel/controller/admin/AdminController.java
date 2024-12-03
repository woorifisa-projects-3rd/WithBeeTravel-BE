package withbeetravel.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.domain.LoginLog;
import withbeetravel.dto.request.admin.LoginLogRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.admin.LoginLogResponse;
import withbeetravel.service.admin.AdminService;
import withbeetravel.service.loginLog.LoginLogService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/logs/all")
    public SuccessResponse<Page<LoginLogResponse>> showAllLoginHistories(
            @RequestParam Long userId,
            @RequestParam int page,
            @RequestParam int size
            ){
        LoginLogRequest loginLogRequest = new LoginLogRequest(userId, page-1, size);
        return SuccessResponse.of(
                HttpStatus.OK.value(),
                "userId의 전체 로그인 로그 조회 성공",
                adminService.showAllLoginHistories(loginLogRequest)
        );

    }
}
