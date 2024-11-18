package withbeetravel.controller.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.dto.request.travel.TravelRequestDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.TravelResponseDto;
import withbeetravel.service.travel.TravelService;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels")
public class TravelController { // new TravelController();

    private final TravelService travelService; // new TravelService();

    // 여행 생성 요청 처리
    @PostMapping
    public ResponseEntity<TravelResponseDto> saveTravel(@RequestBody TravelRequestDto request) {
        TravelResponseDto response = travelService.saveTravel(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{travel-id}")
    @CheckTravelAccess
    public SuccessResponse<TravelResponseDto> editTravel(@RequestBody TravelRequestDto request, @PathVariable("travel-id") Long travelId) {
        travelService.editTravel(request, travelId);  // 여행 정보 수정
        return  SuccessResponse.of(HttpStatus.OK.value(), "여행 정보를 성공적으로 변경"); // ResponseEntity로 메시지 반환
    }


}
