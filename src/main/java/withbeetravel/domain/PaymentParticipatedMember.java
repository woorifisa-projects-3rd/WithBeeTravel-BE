package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "payment_participated_members")
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

    protected PaymentParticipatedMember() {}

    @Builder
    public PaymentParticipatedMember(Long id, TravelMember travelMember, SharedPayment sharedPayment) {
        this.id = id;
        this.travelMember = travelMember;
        this.sharedPayment = sharedPayment;
    }
}
