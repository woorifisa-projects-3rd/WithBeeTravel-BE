package withbeetravel.service.settlement;

import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.settlement.ShowSettlementDetailResponse;

public interface SettlementService {
    SuccessResponse<ShowSettlementDetailResponse> getSettlementDetails(Long userId, Long travelId);

}