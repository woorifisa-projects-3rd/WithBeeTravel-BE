package withbeetravel.controller.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.dto.request.travel.TravelRequestDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.TravelListResponse;
import withbeetravel.dto.response.travel.TravelResponseDto;
import withbeetravel.service.travel.TravelService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels")
public class TravelController {

    private final TravelService travelService;

    @PostMapping
    public SuccessResponse<TravelResponseDto> saveTravel(@RequestBody TravelRequestDto request) {
        return travelService.saveTravel(request);
    }

    @CheckTravelAccess
    @PatchMapping("/{travelId}")
    public SuccessResponse<Void> editTravel(@PathVariable Long travelId, @RequestBody TravelRequestDto request) {
        // 여행 정보 수정
        return travelService.editTravel(request, travelId); // ResponseEntity로 메시지 반환
    }

    @GetMapping
    public SuccessResponse<List<TravelListResponse>> getTravelList() {
        List<TravelListResponse> travelListResponse = travelService.getTravelList();
        return SuccessResponse.of(HttpStatus.OK.value(), "여행 리스트 조회 성공", travelListResponse);
    }
}
