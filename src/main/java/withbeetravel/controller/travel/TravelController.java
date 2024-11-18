package withbeetravel.controller.travel;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import withbeetravel.dto.request.TravelRequestDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.TravelResponseDto;
import withbeetravel.service.travel.TravelService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels")
public class TravelController {

    private final TravelService travelService;

    // 여행 생성 요청 처리
    @PostMapping
    public SuccessResponse<TravelResponseDto> saveTravel(@RequestBody TravelRequestDto request) {
        TravelResponseDto response = travelService.saveTravel(request);
        return SuccessResponse.of(HttpStatus.OK.value(), "Travel saved successfully", response);
    }
}
