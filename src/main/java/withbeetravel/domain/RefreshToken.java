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

    @Column(name = "token", unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
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
}
