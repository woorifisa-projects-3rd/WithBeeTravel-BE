package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "travel_member_settlement_histories")
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
    private double ownPaymentCost;

    @Column(name = "actual_burden_cost", nullable = false)
    private double actualBurdenCost;

    @Column(name = "is_agreed", nullable = false)
    private int isAgreed;

    protected TravelMemberSettlementHistory() {}

    @Builder
    public TravelMemberSettlementHistory(Long id,
                                         SettlementRequest settlementRequest,
                                         TravelMember travelMember,
                                         double ownPaymentCost,
                                         double actualBurdenCost,
                                         int isAgreed) {
        this.id = id;
        this.settlementRequest = settlementRequest;
        this.travelMember = travelMember;
        this.ownPaymentCost = ownPaymentCost;
        this.actualBurdenCost = actualBurdenCost;
        this.isAgreed = isAgreed;
    }
}
