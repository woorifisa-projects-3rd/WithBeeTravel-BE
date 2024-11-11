package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.Account;

public interface AccountRepository extends JpaRepository<Account,Long> {
}
