package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.Travel;

public interface TravelRepository extends JpaRepository<Travel, Long> {
}
