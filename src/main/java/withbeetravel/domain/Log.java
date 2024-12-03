package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_type", nullable = false)
    private LogType logType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_user_id")
    private User relatedUser;  // 송금자 또는 수취자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private History history;  // 연관된 History ID

    @Column(name = "description")
    private String description; // 추가적인 설명 (예: 실패 이유, 트랜잭션 ID 등)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 로그 발생 시점

    @Column(name = "ip_address")
    private String ipAddress;

    @Builder
    public Log(Long id, LogType logType, User user,
               User relatedUser, History history,
               String description, LocalDateTime createdAt, String ipAddress) {
        this.id = id;
        this.logType = logType;
        this.user = user;
        this.relatedUser = relatedUser;
        this.history = history;
        this.description = description;
        this.createdAt = createdAt;
        this.ipAddress = ipAddress;
    }
}
