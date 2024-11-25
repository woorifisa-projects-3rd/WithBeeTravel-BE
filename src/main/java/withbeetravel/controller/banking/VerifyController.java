package withbeetravel.controller.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.dto.request.account.PinNumberRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.account.PinNumberResponse;
import withbeetravel.service.banking.VerifyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/verify")
public class VerifyController {

    private final VerifyService verifyService;

    @PostMapping("/pin-number")
    public SuccessResponse verifyPin(
            @RequestBody PinNumberRequest pinNumberRequest){
        verifyService.verifyPin( pinNumberRequest.getPinNumber());

        return SuccessResponse.of(HttpStatus.OK.value(), "핀번호 검증 완료");
    }

    @GetMapping("/user-state")
    public SuccessResponse<PinNumberResponse> verifyUser(){
        return SuccessResponse.of(
                HttpStatus.OK.value(),
                "유저가 잠금상태가 아닙니다.",
                verifyService.verifyUser()
        );
    }
}
