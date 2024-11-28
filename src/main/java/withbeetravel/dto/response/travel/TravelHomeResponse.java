package withbeetravel.dto.response.travel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import withbeetravel.domain.SettlementStatus;
import withbeetravel.domain.Travel;
import withbeetravel.domain.TravelMember;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Schema(description = "여행 홈 Response DTO")
public class TravelHomeResponse {

    @Schema(description = "여행 id")
    private Long id;

    @Schema(description = "여행명")
    private String travelName;

    @Schema(description = "여행시작일")
    private LocalDate travelStartDate;

    @Schema(description = "여행종료일")
    private LocalDate travelEndDate;

    @Schema(description = "국내여행여부")
    private Boolean isDomesticTravel;

    @Schema(description = "여행지 리스트")
    private List<String> countries;

    @Schema(description = "대표 이미지 URL")
    private String mainImage;

    @Schema(
            description = "지출 항목별 비율 통계",
            example = "{'식비': 30.5, '숙박': 40.2, '교통': 29.3}"
    )
    private Map<String, Double> statistics;

    @Schema(description = "여행 멤버 리스트")
    private List<TravelMemberResponse> travelMembers;

    @Schema(description = "본인이 그룹장인지 여부")
    private boolean isCaptain;

    @Schema(description = "정산이 진행 중인지 여부")
    private SettlementStatus settlementStatus;

    @Schema(description = "정산 내역에 동의했는지 여부")
    private Boolean isAgreed;

    public static TravelHomeResponse of(Travel travel, TravelMember travelMember, Map<String, Double> statistics) {
        return TravelHomeResponse.builder()
                .id(travel.getId())
                .travelName(travel.getTravelName())
                .travelStartDate(travel.getTravelStartDate())
                .travelEndDate(travel.getTravelEndDate())
                .isDomesticTravel(travel.isDomesticTravel())
                .countries(travel.getCountries()
                        .stream().map(country -> country.getCountry().getCountryName())
                        .toList())
                .mainImage(travel.getMainImage())
                .statistics(statistics)
                .travelMembers(travel.getTravelMembers()
                        .stream().map(TravelMemberResponse::from)
                        .toList())
                .isCaptain(travelMember.isCaptain())
                .settlementStatus(travel.getSettlementStatus())
                .isAgreed((travelMember.getSettlementHistory() != null) ? travelMember.getSettlementHistory().isAgreed() : false)
                .build();
    }
}
