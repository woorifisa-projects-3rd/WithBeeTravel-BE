package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expiration_time", nullable = false)
    private Date expirationTime;

    @Builder
    public RefreshToken(Long id, User user, String token, Date expirationTime) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.expirationTime = expirationTime;
    }

    public void update(String token, Date expirationTime) {
        this.token = token;
        this.expirationTime = expirationTime;
    }
}
