package withbeetravel.support;

import withbeetravel.domain.LogTitle;
import withbeetravel.domain.SettlementRequestLog;
import withbeetravel.domain.Travel;
import withbeetravel.domain.User;

import java.time.LocalDateTime;

public class SettlementRequestLogFixture {

    private Long id;
    private Travel travel;
    private User user;
    private LogTitle logTitle = LogTitle.SETTLEMENT_REQUEST;
    private String logMessage = "정산 요청";
    private LocalDateTime logTime = LocalDateTime.now();
    private String link = "travel/1/settlement";

    public static SettlementRequestLogFixture builder() {
        return new SettlementRequestLogFixture();
    }

    public SettlementRequestLogFixture id(Long id) {
        this.id = id;
        return this;
    }

    public SettlementRequestLogFixture travel(Travel travel) {
        this.travel = travel;
        return this;
    }

    public SettlementRequestLogFixture user(User user) {
        this.user = user;
        return this;
    }

    public SettlementRequestLogFixture logTitle(LogTitle logTitle) {
        this.logTitle = logTitle;
        return this;
    }

    public SettlementRequestLogFixture logMessage(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }

    public SettlementRequestLogFixture logTime(LocalDateTime logTime) {
        this.logTime = logTime;
        return this;
    }

    public SettlementRequestLogFixture link(String link) {
        this.link = link;
        return this;
    }

    public SettlementRequestLog build() {
        return SettlementRequestLog.builder()
                .id(id)
                .travel(travel)
                .user(user)
                .logTitle(logTitle)
                .logMessage(logMessage)
                .logTime(logTime)
                .link(link)
                .build();
    }
}
