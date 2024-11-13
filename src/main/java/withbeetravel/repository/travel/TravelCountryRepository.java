package withbeetravel.repository.travel;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.TravelCountry;

import java.util.List;

public interface TravelCountryRepository extends JpaRepository<TravelCountry, Long> {
    List<TravelCountry> findByTravelId(Long travelId);
}
