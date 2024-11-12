package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "rcv_am")
    private Integer rcvAm;

    @Column(name = "pay_am")
    private Integer payAm;

    @Column(name = "balance", nullable = false)
    private int balance;

    @Column(name = "rqspe_nm", nullable = false)
    private String rqspeNm;

    @Column(name = "is_wibee_card", nullable = false)
    private boolean isWibeeCard;

    @Builder
    public History(Long id,
                   Account account,
                   LocalDateTime date,
                   Integer rcvAm,
                   Integer payAm,
                   int balance,
                   String rqspeNm,
                   boolean isWibeeCard) {
        this.id = id;
        this.account = account;
        this.date = date;
        this.rcvAm = rcvAm;
        this.payAm = payAm;
        this.balance = balance;
        this.rqspeNm = rqspeNm;
        this.isWibeeCard = isWibeeCard;
    }
}