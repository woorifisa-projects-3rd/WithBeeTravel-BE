package withbeetravel.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "travels")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id", nullable = false)
    private Long id;

    @Column(name = "travel_name", nullable = false)
    private String travelName;

    @Column(name = "travel_start_date", nullable = false)
    private LocalDate travelStartDate;

    @Column(name = "travel_end_date", nullable = false)
    private LocalDate travelEndDate;

    @Column(name = "invite_code", nullable = false)
    private String inviteCode;

    @Column(name = "main_image")
    private String mainImage;

    @Column(name = "is_domestic_travel", nullable = false)
    private boolean isDomesticTravel;

    @Column(name = "settlement_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SettlementStatus settlementStatus;

    @OneToMany(mappedBy = "travel")
    private List<TravelMember> travelMembers = new ArrayList<>();

    @OneToMany(mappedBy = "travel")
    private List<TravelCountry> countries = new ArrayList<>();

    @Builder
    public Travel(Long id,
                  String travelName,
                  LocalDate travelStartDate,
                  LocalDate travelEndDate,
                  String inviteCode,
                  String mainImage,
                  boolean isDomesticTravel,
                  SettlementStatus settlementStatus) {
        this.id = id;
        this.travelName = travelName;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
        this.inviteCode = inviteCode;
        this.mainImage = mainImage;
        this.isDomesticTravel = isDomesticTravel;
        this.settlementStatus = settlementStatus;
    }

    public void updateTravel(String travelName, LocalDate travelStartDate, LocalDate travelEndDate, boolean isDomesticTravel) {
        this.travelName = travelName;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
        this.isDomesticTravel = isDomesticTravel;
    }



    public void updateMainImage(String newMainImage) {
        this.mainImage = newMainImage;
    }

    public void updateSettlementStatus(SettlementStatus newSettlementStatus) {
        this.settlementStatus = newSettlementStatus;
    }
}
