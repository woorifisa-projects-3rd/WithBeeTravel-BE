package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "settlement_request_logs")
public class SettlementRequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlement_request_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_request_id", nullable = false)
    private SettlementRequest settlementRequest;

    @Column(name = "log_message", nullable = false)
    private String logMessage;

    @Column(name = "log_time", nullable = false)
    private LocalDateTime logTime;

    protected SettlementRequestLog() {}

    @Builder
    public SettlementRequestLog(Long id, SettlementRequest settlementRequest, String logMessage, LocalDateTime logTime) {
        this.id = id;
        this.settlementRequest = settlementRequest;
        this.logMessage = logMessage;
        this.logTime = logTime;
    }
}
