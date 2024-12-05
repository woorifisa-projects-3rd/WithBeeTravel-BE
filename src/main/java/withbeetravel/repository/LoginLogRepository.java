package withbeetravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import withbeetravel.domain.LoginLog;
import withbeetravel.domain.LoginLogType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoginLogRepository extends JpaRepository<LoginLog,Long> {
    Page<LoginLog> findAllByUser_Id(Long id, Pageable pageable);

    Page<LoginLog> findAllByUser_IdAndLoginLogType(Long userId, LoginLogType loginLogType, Pageable pageable);

    @Query("SELECT COUNT(l) FROM LoginLog l WHERE l.loginLogType IN :types")
    long countByLoginLogTypeIn(@Param("types") List<LoginLogType> loginLogTypes);

    @Query("SELECT l.createdAt FROM LoginLog l WHERE l.user.id = :userId ORDER BY l.createdAt ASC")
    LocalDateTime findOldestLoginLogCreatedAtByUserId(@Param("userId") Long userId);

    @Query("SELECT l FROM LoginLog l WHERE l.user.id = :userId AND l.loginLogType = 'REGISTER' ORDER BY l.createdAt ASC LIMIT 1")
    Optional<LoginLog> findFirstRegisterLogByUserId(@Param("userId") Long userId);

    @Query("SELECT l FROM LoginLog l WHERE l.user.id = :userId AND l.loginLogType = 'LOGIN' ORDER BY l.createdAt DESC LIMIT 1")
    Optional<LoginLog> findMostRecentLoginLogByUserId(@Param("userId") Long userId);
}
