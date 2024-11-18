package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.TravelMemberSettlementHistory;

import java.util.List;

public interface TravelMemberSettlementHistoryRepository extends JpaRepository<TravelMemberSettlementHistory, Long> {
    List<TravelMemberSettlementHistory> findAllBySettlementRequestId(Long settlementRequestId);
}