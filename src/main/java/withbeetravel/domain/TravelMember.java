package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "travel_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "connected_account", nullable = false)
    private String connectedAccount;

    @Builder
    public TravelMember(Long id, Travel travel, User user, boolean isCaptain, String connectedAccount) {
        this.id = id;
        this.travel = travel;
        this.user = user;
        this.isCaptain = isCaptain;
        this.connectedAccount = connectedAccount;
    }
}
