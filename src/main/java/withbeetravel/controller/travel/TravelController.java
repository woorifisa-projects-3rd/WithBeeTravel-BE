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

    private final Long userId = 1L;

    // 여행 생성 요청 처리
    @PostMapping
    public ResponseEntity<TravelResponseDto> saveTravel(@RequestBody TravelRequestDto request) {
        TravelResponseDto response = travelService.saveTravel(request);
        return ResponseEntity.ok(response);
    }

    @CheckTravelAccess
    @PatchMapping("/{travelId}")
    public SuccessResponse<Void> editTravel(@PathVariable Long travelId, @RequestBody TravelRequestDto request) {
          // 여행 정보 수정
        return travelService.editTravel(request, travelId); // ResponseEntity로 메시지 반환
    }


}
