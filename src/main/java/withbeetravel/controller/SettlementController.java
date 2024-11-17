package withbeetravel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.settlement.ShowSettlementDetailResponse;
import withbeetravel.service.settlement.SettlementService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travels/{travelId}/settlements")
public class SettlementController {
    private final SettlementService settlementService;

    private final Long userId = 1L;

    @GetMapping
    @CheckTravelAccess
    SuccessResponse<ShowSettlementDetailResponse> getSettlementDetails(@PathVariable Long travelId) {
        return settlementService.getSettlementDetails(userId, travelId);
    }

    @PostMapping
    @CheckTravelAccess
    SuccessResponse requestSettlement(@PathVariable Long travelId) {
        return settlementService.requestSettlement(userId, travelId);
    }
}