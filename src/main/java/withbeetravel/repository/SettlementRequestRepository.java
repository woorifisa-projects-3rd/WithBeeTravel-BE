package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.SettlementRequest;

import java.util.Optional;

public interface SettlementRequestRepository extends JpaRepository<SettlementRequest, Long> {
    Optional<SettlementRequest> findByTravelId(Long travelId);
}