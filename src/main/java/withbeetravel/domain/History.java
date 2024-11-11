package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
    @Column(name = "history_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "date",nullable = false)
    private LocalDateTime date;

    @Column(name = "rcv_am")
    private int rcvAm;

    @Column(name = "pay_am")
    private int payAM;

    @Column(name = "balance", nullable = false)
    private long balance;

    @Column(name = "rqspe_nm", nullable = false)
    private String rqspeNm;

    @Column(name = "is_wibee_card", nullable = false)
    private boolean isWibeeCard;





}
