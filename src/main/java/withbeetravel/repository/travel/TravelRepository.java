package withbeetravel.repository.travel;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.Travel;

import java.util.List;

public interface TravelRepository extends JpaRepository<Travel, Long> {
    List<Travel> findTravelId(Long travelId);
}