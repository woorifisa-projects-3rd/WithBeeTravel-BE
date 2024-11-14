package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import withbeetravel.domain.TravelMember;

import java.util.Optional;

@Repository
public interface TravelMemberRepository extends JpaRepository<TravelMember, Long> {

    Optional<TravelMember> findByTravel_IdAndUser_Id(Long travelId, Long userId);
}