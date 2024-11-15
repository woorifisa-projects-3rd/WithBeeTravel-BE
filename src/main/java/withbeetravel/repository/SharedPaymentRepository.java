package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import withbeetravel.domain.SharedPayment;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharedPaymentRepository extends JpaRepository<SharedPayment, Long> {

    public Optional<SharedPayment> findByIdAndTravelId(Long id, Long travelId);
    public Optional<List<SharedPayment>> findAllByTravelId(Long travelId);
}
