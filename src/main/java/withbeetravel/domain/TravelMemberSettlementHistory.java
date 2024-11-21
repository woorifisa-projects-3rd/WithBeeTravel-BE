package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "travel_member_settlement_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelMemberSettlementHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_member_settlement_history_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_request_id", nullable = false)
    private SettlementRequest settlementRequest;

    @OneToOne
    @JoinColumn(name = "travel_member_id")
    private TravelMember travelMember;

    @Column(name = "own_payment_cost", nullable = false)
    private int ownPaymentCost;

    @Column(name = "actual_burden_cost", nullable = false)
    private int actualBurdenCost;

    @Column(name = "is_agreed", nullable = false)
    private boolean isAgreed;

    @Builder
    public TravelMemberSettlementHistory(Long id,
                                         SettlementRequest settlementRequest,
                                         TravelMember travelMember,
                                         int ownPaymentCost,
                                         int actualBurdenCost,
                                         boolean isAgreed) {
        this.id = id;
        this.settlementRequest = settlementRequest;
        this.travelMember = travelMember;
        this.ownPaymentCost = ownPaymentCost;
        this.actualBurdenCost = actualBurdenCost;
        this.isAgreed = isAgreed;
    }

    public void updateIsAgreed(boolean isAgreed) {
        this.isAgreed = isAgreed;
    }
}
