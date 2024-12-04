package withbeetravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import withbeetravel.domain.LoginLog;
import withbeetravel.domain.LoginLogType;

import java.util.List;

public interface LoginLogRepository extends JpaRepository<LoginLog,Long> {
    Page<LoginLog> findAllByUser_Id(Long id, Pageable pageable);

    Page<LoginLog> findAllByUser_IdAndLoginLogType(Long userId, LoginLogType loginLogType, Pageable pageable);

    @Query("SELECT COUNT(l) FROM LoginLog l WHERE l.loginLogType IN :types")
    long countByLoginLogTypeIn(@Param("types") List<LoginLogType> loginLogTypes);

}
