package withbeetravel.service.settlement;

import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.settlement.ShowSettlementDetailResponse;

public interface SettlementService {
    SuccessResponse<ShowSettlementDetailResponse> getSettlementDetails(Long userId, Long travelId);
    SuccessResponse<Void> requestSettlement(Long userId, Long travelId);

}