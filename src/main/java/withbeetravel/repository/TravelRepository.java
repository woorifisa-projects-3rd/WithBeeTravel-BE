package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import withbeetravel.domain.Travel;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
}
