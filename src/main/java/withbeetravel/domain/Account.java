package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "account_number",nullable = false)
    private String accountNumber;

    @Column(name = "balance",nullable = false)
    private long balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "product", nullable = false)
    private Product product;

    @Column(name = "is_connected_wibee_card", nullable = false)
    private boolean isConnectedWibeeCard;


}
