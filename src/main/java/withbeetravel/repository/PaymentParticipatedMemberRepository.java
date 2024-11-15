package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import withbeetravel.domain.PaymentParticipatedMember;

@Repository
public interface PaymentParticipatedMemberRepository extends JpaRepository<PaymentParticipatedMember, Long> {
}
