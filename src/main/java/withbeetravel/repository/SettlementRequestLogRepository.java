package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.SettlementRequestLog;

public interface SettlementRequestLogRepository extends JpaRepository<SettlementRequestLog, Long> {
}
