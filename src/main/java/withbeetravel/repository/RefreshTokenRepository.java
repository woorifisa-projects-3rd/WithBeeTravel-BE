package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import withbeetravel.domain.RefreshToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByUserId(Long userId);

    @Modifying
    @Query("delete from RefreshToken r where r.user.id = :userId")
    void deleteByUserId(Long userId);

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteAllByExpirationTimeBefore(LocalDateTime localDateTime);
}
