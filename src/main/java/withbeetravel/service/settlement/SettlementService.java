package withbeetravel.service.settlement;

import withbeetravel.dto.settlement.ShowMyTotalPaymentResponse;

public interface SettlementService {
    ShowMyTotalPaymentResponse getMyTotalPayments(Long userId, Long travelId, Long settlementRequestId);

}
