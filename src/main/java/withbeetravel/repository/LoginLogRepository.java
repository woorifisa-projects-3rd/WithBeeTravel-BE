package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.LoginLog;

public interface LoginLogRepository extends JpaRepository<LoginLog,Long> {
}
