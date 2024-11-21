package withbeetravel.dto.request.settlementRequestLog;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SettlementRequestLogDto {
    private Long id;
    private LocalDateTime logTime;
    private String logTitle;
    private String logMessage;
    private String link;

    @Builder
    public SettlementRequestLogDto(Long id, LocalDateTime logTime, String logTitle, String logMessage, String link) {
        this.id = id;
        this.logTime = logTime;
        this.logTitle = logTitle;
        this.logMessage = logMessage;
        this.link = link;
    }
}
