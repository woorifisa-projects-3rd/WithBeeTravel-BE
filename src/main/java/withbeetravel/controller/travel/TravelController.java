package withbeetravel.controller.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.dto.request.travel.InviteCodeSignUpRequestDto;
import withbeetravel.dto.request.travel.TravelRequestDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.InviteCodeSignUpResponseDto;
import withbeetravel.dto.response.travel.TravelResponseDto;
import withbeetravel.service.travel.TravelService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels")
public class TravelController {

    private final TravelService travelService;

    @PostMapping
    public SuccessResponse<TravelResponseDto> saveTravel(@RequestBody TravelRequestDto request) {
        TravelResponseDto travelResponseDto = travelService.saveTravel(request);
        return SuccessResponse.of(HttpStatus.OK.value(), "여행 생성 성공",travelResponseDto);
    }

    @CheckTravelAccess
    @PatchMapping("/{travelId}")
    public SuccessResponse<Void> editTravel(@PathVariable Long travelId, @RequestBody TravelRequestDto request) {
        // 여행 정보 수정
        travelService.editTravel(request, travelId);
        return SuccessResponse.of(HttpStatus.OK.value(), "여행 생성 성공");
    }

    @PostMapping("/{travelId}/invite-code")
    public SuccessResponse<InviteCodeSignUpResponseDto> signUpTravel(@RequestBody InviteCodeSignUpRequestDto request){
        InviteCodeSignUpResponseDto inviteCodeResponseDto = travelService.signUpTravel(request);
        return SuccessResponse.of(HttpStatus.OK.value(), "여행 가입 성공", inviteCodeResponseDto);
    }

    @GetMapping("/{travelId}/invite-code")
    public  SuccessResponse<>


}
