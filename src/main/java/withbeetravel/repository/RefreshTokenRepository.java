package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByUserId(Long userId);
    void deleteByUserId(Long userId);

    Optional<RefreshToken> findByToken(String token);
}
