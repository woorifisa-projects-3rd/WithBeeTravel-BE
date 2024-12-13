package withbeetravel.service.settlement;

import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.settlement.ShowSettlementDetailResponse;

public interface SettlementService {
    ShowSettlementDetailResponse getSettlementDetails(Long userId, Long travelId);
    void requestSettlement(Long userId, Long travelId);

    String agreeSettlement(Long userId, Long travelId);

    void cancelSettlement(Long userId, Long travelId);
}