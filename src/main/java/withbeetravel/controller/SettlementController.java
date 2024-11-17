package withbeetravel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.settlement.ShowSettlementDetailResponse;
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
}