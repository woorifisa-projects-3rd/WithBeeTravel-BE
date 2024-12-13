package withbeetravel.dto.request.settlementRequestLog;

import lombok.Builder;
import lombok.Getter;
import withbeetravel.domain.SettlementRequestLog;

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

    public static SettlementRequestLogDto of (SettlementRequestLog settlementRequestLog, String link) {
        return SettlementRequestLogDto.builder()
                .id(settlementRequestLog.getId())
                .logTime(settlementRequestLog.getLogTime())
                .logTitle(settlementRequestLog.getLogTitle().getTitle())
                .logMessage(settlementRequestLog.getLogMessage())
                .link(link)
                .build();
    }
}
