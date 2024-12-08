package withbeetravel.support;

import withbeetravel.domain.SettlementStatus;
import withbeetravel.domain.Travel;

import java.time.LocalDate;

public class TravelFixture {

    private Long id;
    private String travelName = "강릉 여행";
    private LocalDate travelStartDate = LocalDate.now();
    private LocalDate travelEndDate = LocalDate.now().plusDays(4);
    private String inviteCode = "INV123456";
    private String mainImage = null;
    private boolean isDomesticTravel = true;
    private SettlementStatus settlementStatus = SettlementStatus.PENDING;

    public static TravelFixture builder() {
        return new TravelFixture();
    }

    public TravelFixture id(Long id) {
        this.id = id;
        return this;
    }

    public TravelFixture travelName(String travelName) {
        this.travelName = travelName;
        return this;
    }

    public TravelFixture travelStartDate(LocalDate travelStartDate) {
        this.travelStartDate = travelStartDate;
        return this;
    }

    public TravelFixture travelEndDate(LocalDate travelEndDate) {
        this.travelEndDate = travelEndDate;
        return this;
    }

    public TravelFixture inviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
        return this;
    }

    public TravelFixture mainImage(String mainImage) {
        this.mainImage = mainImage;
        return this;
    }

    public TravelFixture isDomesticTravel(boolean isDomesticTravel) {
        this.isDomesticTravel = isDomesticTravel;
        return this;
    }

    public TravelFixture settlementStatus(SettlementStatus settlementStatus) {
        this.settlementStatus = settlementStatus;
        return this;
    }

    public Travel build() {
        return Travel.builder()
                .id(id)
                .travelName(travelName)
                .travelStartDate(travelStartDate)
                .travelEndDate(travelEndDate)
                .inviteCode(inviteCode)
                .mainImage(mainImage)
                .isDomesticTravel(isDomesticTravel)
                .settlementStatus(settlementStatus)
                .build();
    }
}
