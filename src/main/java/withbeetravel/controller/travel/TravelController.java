package withbeetravel.controller.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.dto.request.travel.InviteCodeSignUpRequest;
import withbeetravel.dto.request.travel.TravelRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.InviteCodeGetResponse;
import withbeetravel.dto.response.travel.InviteCodeSignUpResponse;
import withbeetravel.dto.response.travel.TravelResponse;
import withbeetravel.service.travel.TravelService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels")
public class TravelController {

    private final TravelService travelService;

    @PostMapping
    public SuccessResponse<TravelResponse> saveTravel(@RequestBody TravelRequest request) {
        TravelResponse travelResponse = travelService.saveTravel(request);
        return SuccessResponse.of(HttpStatus.OK.value(), "여행 생성 성공",travelResponse);
    }

    @CheckTravelAccess
    @PatchMapping("/{travelId}")
    public SuccessResponse<Void> editTravel(@PathVariable Long travelId, @RequestBody TravelRequest request) {
        // 여행 정보 수정
        travelService.editTravel(request, travelId);
        return SuccessResponse.of(HttpStatus.OK.value(), "여행 생성 성공");
    }

    @PostMapping("/invite-code")
    public SuccessResponse<InviteCodeSignUpResponse> signUpTravel(@RequestBody InviteCodeSignUpRequest request){
        System.out.println(request);
        InviteCodeSignUpResponse inviteCodeResponseDto = travelService.signUpTravel(request);
        return SuccessResponse.of(HttpStatus.OK.value(), "여행 가입 성공", inviteCodeResponseDto);
    }

    @GetMapping("/{travelId}/invite-code")
    @CheckTravelAccess
    public SuccessResponse<InviteCodeGetResponse> getInviteCode(@PathVariable Long travelId){
        InviteCodeGetResponse inviteCodeGetReponse = travelService.getInviteCode(travelId);
        return SuccessResponse.of(HttpStatus.OK.value(), "초대 코드 조회 성공", inviteCodeGetReponse);
    }


}
