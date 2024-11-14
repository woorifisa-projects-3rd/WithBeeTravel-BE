package withbeetravel.controller.travel;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import withbeetravel.dto.travel.TravelRequestDto;
import withbeetravel.dto.travel.TravelResponseDto;
import withbeetravel.service.travel.TravelService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels")
public class TravelController {

    private final TravelService travelService;

    // 여행 생성 요청 처리
    @PostMapping
    public ResponseEntity<TravelResponseDto> saveTravel(@RequestBody @Valid TravelRequestDto request) {
        TravelResponseDto response = travelService.saveTravel(request);
        return ResponseEntity.ok(response);
    }


}
