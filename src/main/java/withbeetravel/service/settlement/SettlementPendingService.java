package withbeetravel.service.settlement;

import withbeetravel.domain.SettlementRequest;
import withbeetravel.domain.SettlementRequestLog;
import withbeetravel.domain.TravelMember;
import withbeetravel.domain.TravelMemberSettlementHistory;

import java.util.List;

public interface SettlementPendingService {

    void handlePendingSettlementRequest(List<SettlementRequestLog> settlementRequestLogs,
                                        List<TravelMember> insufficientBalanceMembers,
                                        SettlementRequest settlementRequest,
                                        int updatedCount,
                                        TravelMemberSettlementHistory travelMemberSettlementHistory);
}
