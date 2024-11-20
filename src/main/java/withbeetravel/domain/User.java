package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "pin_number", nullable = false)
    private String pinNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Column(name = "has_wibee_card", nullable = false)
    private boolean hasWibeeCard;

    @Column(name = "failed_pin_count", nullable = false)
    private int failedPinCount;

    @Column(name = "pinLocked", nullable = false)
    private boolean accountLocked;

    @Builder
    public User(Long id, String email, String password, String pinNumber,
                String name, String profileImage, boolean hasWibeeCard,
                int failedPinCount, boolean accountLocked) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.pinNumber = pinNumber;
        this.name = name;
        this.profileImage = profileImage;
        this.hasWibeeCard = hasWibeeCard;
        this.failedPinCount = failedPinCount;
        this.accountLocked = accountLocked;
    }
}
