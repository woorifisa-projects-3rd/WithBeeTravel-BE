package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "account_number", unique = true)
    private String accountNumber;

    @Column(name = "balance", nullable = false)
    private Long balance;

    @Column(name = "product", nullable = false)
    @Enumerated(EnumType.STRING)
    private Product product;

    @Column(name = "is_connected_wibee_card", nullable = false)
    private boolean isConnectedWibeeCard;

    @Builder
    public Account(Long id, User user, String accountNumber, Long balance, Product product, boolean isConnectedWibeeCard) {
        this.id = id;
        this.user = user;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.product = product;
        this.isConnectedWibeeCard = isConnectedWibeeCard;
    }
}