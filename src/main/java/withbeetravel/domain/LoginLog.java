package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "login_logs")
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "log_type", nullable = false)
    private LoginLogType loginLogType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id",nullable = false)
    private User user;

    @Column(name = "description")
    private String description; // 추가적인 설명 (예: 실패 이유, 트랜잭션 ID 등)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 로그 발생 시점

    @Column(name = "ip_address")
    private String ipAddress;

    protected  LoginLog(){};

    @Builder
    public LoginLog(Long id, LoginLogType loginLogType, User user,
                    String description, LocalDateTime createdAt, String ipAddress) {
        this.id = id;
        this.loginLogType = loginLogType;
        this.user = user;
        this.description = description;
        this.createdAt = createdAt;
        this.ipAddress = ipAddress;
    }
}
