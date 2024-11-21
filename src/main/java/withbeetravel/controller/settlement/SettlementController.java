package withbeetravel.controller.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final Long userId = 3L;

    @GetMapping
    @CheckTravelAccess
    SuccessResponse<ShowSettlementDetailResponse> getSettlementDetails(@PathVariable Long travelId) {
        ShowSettlementDetailResponse showSettlementDetailResponse = settlementService.getSettlementDetails(userId, travelId);
        return SuccessResponse.of(HttpStatus.OK.value(), "세부 지출 내역 조회 성공", showSettlementDetailResponse);
    }

    @PostMapping
    @CheckTravelAccess
    SuccessResponse<Void> requestSettlement(@PathVariable Long travelId) {
        settlementService.requestSettlement(userId, travelId);
        return SuccessResponse.of(HttpStatus.OK.value(), "정산 요청 성공");
    }

    @PostMapping("/agreement")
    @CheckTravelAccess
    SuccessResponse<Void> agreeSettlement(@PathVariable Long travelId) {

        String message = settlementService.agreeSettlement(userId, travelId);
        return SuccessResponse.of(HttpStatus.OK.value(), message);
    }
}