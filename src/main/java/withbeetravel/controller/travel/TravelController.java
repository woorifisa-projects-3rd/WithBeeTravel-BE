package withbeetravel.controller.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.dto.request.account.CardCompletedRequest;
import withbeetravel.dto.request.travel.InviteCodeSignUpRequest;
import withbeetravel.dto.request.travel.TravelRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.account.AccountConnectedWibeeResponse;
import withbeetravel.dto.response.travel.TravelHomeResponse;
import withbeetravel.dto.response.travel.InviteCodeGetResponse;
import withbeetravel.dto.response.travel.InviteCodeSignUpResponse;
import withbeetravel.dto.response.travel.TravelResponse;
import withbeetravel.dto.response.travel.TravelListResponse;
import withbeetravel.security.UserAuthorizationUtil;
import withbeetravel.service.travel.TravelService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels")
public class TravelController {

    private final TravelService travelService;

    @CheckTravelAccess
    @GetMapping("/{travelId}")
    public SuccessResponse<TravelHomeResponse> getTravel(@PathVariable Long travelId) {
        Long userId = UserAuthorizationUtil.getLoginUserId();

        return SuccessResponse.of(200, "여행 홈 데이터 불러오기 성공", travelService.getTravel(travelId, userId));
    }

    @PostMapping
    public SuccessResponse<TravelResponse> saveTravel(@RequestBody TravelRequest request) {
        Long userId = UserAuthorizationUtil.getLoginUserId();
        TravelResponse travelResponse = travelService.saveTravel(request,userId);
        return SuccessResponse.of(HttpStatus.OK.value(), "여행 생성 성공",travelResponse);
    }

    @CheckTravelAccess
    @PatchMapping("/{travelId}")
    public SuccessResponse<Void> editTravel(@PathVariable Long travelId, @RequestBody TravelRequest request) {
        // 여행 정보 수정
        travelService.editTravel(request, travelId);
        return SuccessResponse.of(HttpStatus.OK.value(), "여행 편집 성공");
    }

    @PostMapping("/invite-code")
    public SuccessResponse<InviteCodeSignUpResponse> signUpTravel(@RequestBody InviteCodeSignUpRequest request){
        Long userId = UserAuthorizationUtil.getLoginUserId();
        InviteCodeSignUpResponse inviteCodeResponse = travelService.signUpTravel(request,userId);
        return SuccessResponse.of(HttpStatus.OK.value(), "여행 가입 성공", inviteCodeResponse);
    }

    @GetMapping("/{travelId}/invite-code")
    @CheckTravelAccess
    public SuccessResponse<InviteCodeGetResponse> getInviteCode(@PathVariable Long travelId){
        InviteCodeGetResponse inviteCodeGetReponse = travelService.getInviteCode(travelId);
        return SuccessResponse.of(HttpStatus.OK.value(), "초대 코드 조회 성공", inviteCodeGetReponse);
    }


    @GetMapping
    public SuccessResponse<List<TravelListResponse>> getTravelList() {
        Long userId = UserAuthorizationUtil.getLoginUserId();
        List<TravelListResponse> travelListResponse = travelService.getTravelList(userId);
        return SuccessResponse.of(HttpStatus.OK.value(), "여행 리스트 조회 성공", travelListResponse);
    }

//  계좌 연결
    @PostMapping("/accounts")
    public SuccessResponse<Void> postConnectedAccount(@RequestBody CardCompletedRequest request){
        Long userId = UserAuthorizationUtil.getLoginUserId();
        travelService.postConnectedAccount(request,userId);
        return SuccessResponse.of(HttpStatus.OK.value(), "계좌 연결 완료");
    }

//    카드 발급 여부
    @GetMapping("/accounts")
    public SuccessResponse<AccountConnectedWibeeResponse> getConnectedAccount(){
        Long userId = UserAuthorizationUtil.getLoginUserId();
        AccountConnectedWibeeResponse accountConnectedWibeeResponse = travelService.getConnectedWibee(userId);
        return SuccessResponse.of(HttpStatus.OK.value(), "위비 카드 발급 여부 확인",accountConnectedWibeeResponse);
    }
}
