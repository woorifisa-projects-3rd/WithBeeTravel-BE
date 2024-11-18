package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "balance",nullable = false)
    private long balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "product", nullable = false)
    private Product product;

    @Column(name = "is_connected_wibee_card", nullable = false)
    private boolean isConnectedWibeeCard;

    public void transfer(int amount){
        balance += amount;
    }

    protected Account(){};

    @Builder
    public Account(Long id, User user,
                   String accountNumber, long balance,
                   Product product, boolean isConnectedWibeeCard) {
        this.id = id;
        this.user = user;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.product = product;
        this.isConnectedWibeeCard = isConnectedWibeeCard;
    }
}
