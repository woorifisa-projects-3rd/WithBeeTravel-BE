package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.Account;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long> {

    List<Account> findByUserId(Long userId);

}
