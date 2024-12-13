package withbeetravel.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.History;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Long> {
    List<History> findByAccountId(Long accountId);

    List<History> findByAccountIdOrderByDateDesc(Long accountId);

    List<History> findByAccountIdAndDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
}
