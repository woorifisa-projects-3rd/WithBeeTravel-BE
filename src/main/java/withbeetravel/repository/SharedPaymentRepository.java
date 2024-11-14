package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import withbeetravel.domain.SharedPayment;

@Repository
public interface SharedPaymentRepository extends JpaRepository<SharedPayment, Long> {
}
