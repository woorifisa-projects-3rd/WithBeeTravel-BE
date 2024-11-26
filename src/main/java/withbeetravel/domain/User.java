package withbeetravel.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "wibee_card_account")
    @JsonManagedReference
    private Account wibeeCardAccount;

    @OneToOne
    @JoinColumn(name = "connected_account")
    @JsonManagedReference
    private Account connectedAccount;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "pin_number", nullable = false)
    private String pinNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "profile_image", nullable = false)
    private int profileImage;

    @Column(name = "failed_pin_count", nullable = false)
    private int failedPinCount;

    @Column(name = "pin_locked", nullable = false)
    private boolean pinLocked;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;


    @Builder
    public User(Long id, Account wibeeCardAccount, Account connectedAccount, String email,
                String password, String pinNumber, String name,
                int profileImage, int failedPinCount, boolean pinLocked, RoleType roleType) {
        this.id = id;
        this.wibeeCardAccount = wibeeCardAccount;
        this.connectedAccount = connectedAccount;
        this.email = email;
        this.password = password;
        this.pinNumber = BCrypt.hashpw(pinNumber, BCrypt.gensalt());
        this.name = name;
        this.profileImage = profileImage;
        this.failedPinCount = failedPinCount;
        this.pinLocked = pinLocked;
        this.roleType = roleType;
    }

    public void updateWibeeCardAccount(Account account) {
        this.wibeeCardAccount = account;
    }

    public void incrementFailedPinCount() {
        this.failedPinCount++;
        if (this.failedPinCount >= 5) {
            this.pinLocked = true;  // 실패 5회 이상 시 계정 잠금
        }
    }

    public void resetFailedPinCount() {
        this.failedPinCount = 0;
        this.pinLocked = false;  // 계정 잠금 해제
    }

    public boolean validatePin(String rawPin) {
        // 예시: BCrypt로 비교하는 방법
        return BCrypt.checkpw(rawPin, this.pinNumber);
    }

}
