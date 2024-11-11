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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_captain", nullable = false)
    private boolean isCaptain;

    @Column(name = "connected_account", nullable = false)
    private String connectedAccount;

    protected TravelMember() {}

    @Builder
    public TravelMember(Long id, Travel travel, User user, boolean isCaptain, String connectedAccount) {
        this.id = id;
        this.travel = travel;
        this.user = user;
        this.isCaptain = isCaptain;
        this.connectedAccount = connectedAccount;
    }
}
