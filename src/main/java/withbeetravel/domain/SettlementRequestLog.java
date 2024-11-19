package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "settlement_request_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SettlementRequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlement_request_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_request_id", nullable = false)
    private SettlementRequest settlementRequest;

    @Column(name = "log_title", nullable = false)
    @Enumerated(EnumType.STRING)
    private LogTitle logTitle;

    @Column(name = "log_message", nullable = false)
    private String logMessage;

    @CreationTimestamp
    @Column(name = "log_time")
    private LocalDateTime logTime;

    @Builder
    public SettlementRequestLog(Long id,
                                SettlementRequest settlementRequest,
                                LogTitle logTitle,
                                String logMessage) {
        this.id = id;
        this.settlementRequest = settlementRequest;
        this.logTitle = logTitle;
        this.logMessage = logMessage;
    }
}
