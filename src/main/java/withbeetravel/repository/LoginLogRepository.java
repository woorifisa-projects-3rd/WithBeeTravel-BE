package withbeetravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.LoginLog;

public interface LoginLogRepository extends JpaRepository<LoginLog,Long> {
    Page<LoginLog> findAllByUser_Id(Long id, Pageable pageable);
}
