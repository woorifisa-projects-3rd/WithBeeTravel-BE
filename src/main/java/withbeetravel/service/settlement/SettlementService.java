package withbeetravel.service.settlement;

import withbeetravel.dto.settlement.ShowSettlementDetailResponse;

public interface SettlementService {
    ShowSettlementDetailResponse getSettlementDetails(Long userId, Long travelId, Long settlementRequestId);

}