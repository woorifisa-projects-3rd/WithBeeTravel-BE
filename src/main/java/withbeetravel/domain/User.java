package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
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

    @Column(name = "has_card", nullable = false)
    private int hasCard;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    protected User() {}

    @Builder
    public User(Long id, String email, String password, String pinNumber, String name, int hasCard, String profileImage) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.pinNumber = pinNumber;
        this.name = name;
        this.hasCard = hasCard;
        this.profileImage = profileImage;
    }
}
