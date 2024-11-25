package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.SettlementRequestLog;
import java.util.List;

public interface SettlementRequestLogRepository extends JpaRepository<SettlementRequestLog, Long> {
    List<SettlementRequestLog> findAllByTravelId(Long travelId);
}
