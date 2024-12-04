package withbeetravel.support;

import withbeetravel.domain.SettlementRequest;
import withbeetravel.domain.Travel;

import java.time.LocalDateTime;

public class SettlementRequestFixture {

    private Long id;
    private Travel travel;
    private LocalDateTime requestStartTime = LocalDateTime.now();
    private LocalDateTime requestEndTime;
    private int disagreeCount = 0;

    public static SettlementRequestFixture builder() {
        return new SettlementRequestFixture();
    }

    public SettlementRequestFixture id(Long id) {
        this.id = id;
        return this;
    }

    public SettlementRequestFixture travel(Travel travel) {
        this.travel = travel;
        return this;
    }

    public SettlementRequestFixture requestStartTime(LocalDateTime requestStartTime) {
        this.requestStartTime = requestStartTime;
        return this;
    }

    public SettlementRequestFixture requestEndTime(LocalDateTime requestEndTime) {
        this.requestEndTime = requestEndTime;
        return this;
    }

    public SettlementRequestFixture disagreeCount(int disagreeCount) {
        this.disagreeCount = disagreeCount;
        return this;
    }

    public SettlementRequest build() {
        return SettlementRequest.builder()
                .id(id)
                .travel(travel)
                .requestStartTime(requestStartTime)
                .requestEndTime(requestEndTime)
                .disagreeCount(disagreeCount)
                .build();
    }
}
