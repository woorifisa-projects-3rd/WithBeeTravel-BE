package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import withbeetravel.domain.TravelMember;
import java.util.List;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelMemberRepository extends JpaRepository<TravelMember, Long> {

    Optional<TravelMember> findByTravelIdAndUserId(Long travelId, Long userId);
  
    List<TravelMember> findAllByTravelId(Long travelId);

    int countByTravelId(Long travelId);
}
