package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import withbeetravel.domain.TravelMemberSettlementHistory;

import java.util.List;

public interface TravelMemberSettlementHistoryRepository extends JpaRepository<TravelMemberSettlementHistory, Long> {
    List<TravelMemberSettlementHistory> findAllBySettlementRequestId(Long settlementRequestId);
    TravelMemberSettlementHistory findTravelMemberSettlementHistoryBySettlementRequestIdAndTravelMemberId(Long settlementRequestId, Long travelMemberId);

    @Query("SELECT t FROM TravelMemberSettlementHistory t WHERE t.settlementRequest.id = :settlementRequestId " +
            "ORDER BY (t.ownPaymentCost - t.actualBurdenCost)")
    List<TravelMemberSettlementHistory>
    findAllBySettlementRequestIdOrderByCalculatedCost(@Param("settlementRequestId") Long settlementRequestId);
}
