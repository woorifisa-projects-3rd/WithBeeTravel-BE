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
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
                                Travel travel,
                                User user,
                                LogTitle logTitle,
                                String logMessage,
                                LocalDateTime logTime) {
        this.id = id;
        this.travel = travel;
        this.user = user;
        this.logTitle = logTitle;
        this.logMessage = logMessage;
        this.logTime = logTime;
    }
}
