package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.TravelMember;

import java.util.Optional;

public interface TravelMemberRepository extends JpaRepository<TravelMember, Long> {
    Optional<TravelMember> findByUserIdAndTravelId(Long userId, Long travelId);
}
