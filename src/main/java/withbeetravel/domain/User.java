package withbeetravel.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToOne
    @JoinColumn(name = "wibee_card_account")
    @JsonManagedReference
    private Account wibeeCardAccount;

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

    @Builder
    public User(Long id,
                Account wibeeCardAccount,
                String email,
                String password,
                String pinNumber,
                String name,
                int profileImage) {
        this.id = id;
        this.wibeeCardAccount = wibeeCardAccount;
        this.email = email;
        this.password = password;
        this.pinNumber = pinNumber;
        this.name = name;
        this.profileImage = profileImage;
    }

    public void updateWibeeCardAccount(Account account) {
        this.wibeeCardAccount = account;
    }
}
