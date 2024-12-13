package withbeetravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import withbeetravel.domain.Category;
import withbeetravel.domain.SharedPayment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SharedPaymentRepository extends JpaRepository<SharedPayment, Long> {

    public Optional<SharedPayment> findByIdAndTravelId(Long id, Long travelId);

    List<SharedPayment> findAllByTravelId(Long travelId);

    List<SharedPayment> findAllByAddedByMemberId(Long addedByMemberId);


    // 결제 내역 조회
    @Query("SELECT DISTINCT sp FROM SharedPayment sp " +
            "LEFT JOIN FETCH sp.paymentParticipatedMembers ppm " +
            "LEFT JOIN FETCH ppm.travelMember " +
            "WHERE sp.travel.id = :travelId " +
            "AND (:memberId IS NULL OR EXISTS (SELECT 1 FROM PaymentParticipatedMember pm " +
            "                                 WHERE pm.sharedPayment = sp " +
            "                                 AND pm.travelMember.id = :memberId)) " +
            "AND (:startDate IS NULL OR DATE(sp.paymentDate) >= :startDate) " +
            "AND (:endDate IS NULL OR DATE(sp.paymentDate) <= :endDate)" +
            "AND (COALESCE(:category, sp.category) = sp.category)")
    Page<SharedPayment> findAllByTravelIdAndMemberIdAndDateRange(
            @Param("travelId") Long travelId,
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("category") Category category,
            Pageable pageable
    );
}
