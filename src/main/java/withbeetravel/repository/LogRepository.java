package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.Log;

public interface LogRepository extends JpaRepository<Log,Long> {
}
