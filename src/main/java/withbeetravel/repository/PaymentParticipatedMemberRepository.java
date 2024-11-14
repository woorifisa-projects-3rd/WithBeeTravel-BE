package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.PaymentParticipatedMember;
import java.util.List;

public interface PaymentParticipatedMemberRepository extends JpaRepository<PaymentParticipatedMember, PaymentParticipatedMember> {
    List<PaymentParticipatedMember> findAllByTravelMemberId(Long travelMemberId);
}
