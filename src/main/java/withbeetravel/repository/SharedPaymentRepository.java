package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import withbeetravel.domain.SharedPayment;

import java.util.Optional;
import java.util.List;

@Repository
public interface SharedPaymentRepository extends JpaRepository<SharedPayment, Long> {

    public Optional<SharedPayment> findByIdAndTravelId(Long id, Long travelId);

    List<SharedPayment> findAllByAddedByMemberId(Long addedByMemberId);
}
