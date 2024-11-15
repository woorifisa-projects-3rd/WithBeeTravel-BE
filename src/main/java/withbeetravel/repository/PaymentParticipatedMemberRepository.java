package withbeetravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import withbeetravel.domain.PaymentParticipatedMember;

import java.util.List;

@Repository
public interface PaymentParticipatedMemberRepository extends JpaRepository<PaymentParticipatedMember, Long> {

    public List<PaymentParticipatedMember> findAllBySharedPaymentId(Long sharedPaymentId);
}
