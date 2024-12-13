package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "travel_members")
public class TravelMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_member_id", nullable = false)
    private Long id;

    @JoinColumn(name = "travel_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Travel travel;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "is_captain", nullable = false)
    private boolean isCaptain;

    @OneToOne(mappedBy = "travelMember")
    private TravelMemberSettlementHistory settlementHistory;

    protected TravelMember() {}

    @Builder
    public TravelMember(Long id, Travel travel, User user, boolean isCaptain) {
        this.id = id;
        this.travel = travel;
        this.user = user;
        this.isCaptain = isCaptain;
    }

    public void initializeSettlementHistory() {
        settlementHistory = null;
    }
}
