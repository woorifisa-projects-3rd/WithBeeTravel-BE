package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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
    private Integer rcvAm;

    @Column(name = "pay_am")
    private Integer payAM;

    @Column(name = "balance", nullable = false)
    private long balance;

    @Column(name = "rqspe_nm", nullable = false)
    private String rqspeNm;

    @Column(name = "is_wibee_card", nullable = false)
    private boolean isWibeeCard;





}
