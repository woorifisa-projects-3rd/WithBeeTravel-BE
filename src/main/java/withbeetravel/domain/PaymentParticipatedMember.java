package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "payment_participated_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentParticipatedMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_participated_member_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_member_id", nullable = false)
    private TravelMember travelMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_payment_id", nullable = false)
    private SharedPayment sharedPayment;

    @Builder
    public PaymentParticipatedMember(Long id, TravelMember travelMember, SharedPayment sharedPayment) {
        this.id = id;
        this.travelMember = travelMember;
        this.sharedPayment = sharedPayment;
    }
}
