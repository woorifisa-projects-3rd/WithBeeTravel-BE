package withbeetravel.controller.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.dto.request.travel.TravelRequestDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.TravelResponseDto;
import withbeetravel.service.travel.TravelService;

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

}
