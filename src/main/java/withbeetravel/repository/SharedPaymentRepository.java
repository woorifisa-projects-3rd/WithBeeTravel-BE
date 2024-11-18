package withbeetravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import withbeetravel.domain.SharedPayment;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharedPaymentRepository extends JpaRepository<SharedPayment, Long> {

    public Optional<SharedPayment> findByIdAndTravelId(Long id, Long travelId);

    // 결제 내역 조회
    @Query("SELECT DISTINCT sp FROM SharedPayment sp " +
            "LEFT JOIN FETCH sp.paymentParticipatedMembers ppm " +
            "LEFT JOIN FETCH ppm.travelMember " +
            "WHERE sp.travel.id = :travelId")
    public Page<SharedPayment> findAllByTravelId(@Param("travelId") Long travelId, Pageable pageable);

    // 특정 멤버가 참여한 결제 내역 조회
    @Query("SELECT DISTINCT sp FROM SharedPayment sp " +
            "LEFT JOIN FETCH sp.paymentParticipatedMembers ppm " +
            "LEFT JOIN FETCH ppm.travelMember tm " +
            "WHERE sp.travel.id = :travelId " +
            "AND EXISTS (SELECT 1 FROM PaymentParticipatedMember pm " +
            "           WHERE pm.sharedPayment = sp " +
            "           AND pm.travelMember.id = :memberId)")
    Page<SharedPayment> findByTravelIdAndMemberId(
            @Param("travelId") Long travelId,
            @Param("memberId") Long memberId,
            Pageable pageable
    );
}
