package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.Travel;
import withbeetravel.domain.TravelCountry;

import java.util.List;

public interface TravelCountryRepository extends JpaRepository<TravelCountry, Long> {
    List<TravelCountry> findByTravelId(Long travelId);
    void deleteByTravel(Travel travel);
}
