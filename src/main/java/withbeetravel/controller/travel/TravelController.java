package withbeetravel.controller.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import withbeetravel.dto.request.travel.TravelRequestDto;
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
    public ResponseEntity<Map<String, String>> editTravel(@RequestBody TravelRequestDto request, @PathVariable("travel-id") Long travelId) {
        travelService.editTravel(request, travelId);  // 여행 정보 수정

        // 성공 메시지 반환
        Map<String, String> response = new HashMap<>();
        response.put("status", "200");
        response.put("message", "여행 정보를 성공적으로 변경했습니다.");

        return ResponseEntity.ok(response);  // ResponseEntity로 메시지 반환
    }


}
