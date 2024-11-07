package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id", nullable = false)
    private Long id;

    @Column(name = "travel_name", nullable = false)
    private String travelName;

    @Column(name = "travel_start_date", nullable = false)
    private Date travelStartDate;

    @Column(name = "travel_end_date", nullable = false)
    private Date travelEndDate;

    @Column(name = "invite_code", nullable = false)
    private String inviteCode;

    @Column(name = "main_image")
    private String mainImage;

    @Column(name = "is_domestic_travel", nullable = false)
    private int isDomesticTravel;

    @Column(name = "settlement_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SettlementStatus settlementStatus;

    protected Travel() {}

    public Travel(Long id,
                  String travelName,
                  Date travelStartDate,
                  Date travelEndDate,
                  String inviteCode,
                  String mainImage, int isDomesticTravel, SettlementStatus settlementStatus) {
        this.id = id;
        this.travelName = travelName;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
        this.inviteCode = inviteCode;
        this.mainImage = mainImage;
        this.isDomesticTravel = isDomesticTravel;
        this.settlementStatus = settlementStatus;
    }
}
