package withbeetravel.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import withbeetravel.dto.travel.TravelRequestDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels")
public class TravelController {

    private final TravelService travelService;

    @PostMapping
    public ResponseEntity<String> createTravel(@RequestBody @Valid TravelRequestDto request) {
        travelService.saveTravel(request);
        return ResponseEntity.ok().build();
    }
}
