package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.SettlementRequest;

public interface SettlementRequestRepository extends JpaRepository<SettlementRequest, Long> {
}