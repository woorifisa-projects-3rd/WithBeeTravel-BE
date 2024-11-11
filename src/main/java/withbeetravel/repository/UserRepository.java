package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
