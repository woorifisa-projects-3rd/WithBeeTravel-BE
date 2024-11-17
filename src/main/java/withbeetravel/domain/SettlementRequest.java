package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "settlement_requests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettlementRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlement_request_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @CreationTimestamp
    @Column(name = "request_start_time")
    private LocalDateTime requestStartTime;

    @Column(name = "request_end_time")
    private LocalDateTime requestEndTime;

    @Column(name = "disagree_count", nullable = false)
    private int disagreeCount;

    @Builder
    public SettlementRequest(Long id,
                             Travel travel,
                             LocalDateTime requestStartTime,
                             LocalDateTime requestEndTime,
                             int disagreeCount) {
        this.id = id;
        this.travel = travel;
        this.requestStartTime = requestStartTime;
        this.requestEndTime = requestEndTime;
        this.disagreeCount = disagreeCount;
    }
}
