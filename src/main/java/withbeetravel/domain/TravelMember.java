package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
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
    private int isCaptain;

    @Column(name = "connected_account", nullable = false)
    private String connectedAccount;

    protected TravelMember() {}

    @Builder
    public TravelMember(Long id, Travel travel, User user, int isCaptain, String connectedAccount) {
        this.id = id;
        this.travel = travel;
        this.user = user;
        this.isCaptain = isCaptain;
        this.connectedAccount = connectedAccount;
    }
}
