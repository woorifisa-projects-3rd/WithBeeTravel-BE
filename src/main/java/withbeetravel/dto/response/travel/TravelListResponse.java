package withbeetravel.dto.response.travel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import withbeetravel.domain.Travel;
import withbeetravel.domain.TravelCountry;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@AllArgsConstructor
public class TravelListResponse {
    private Long travelId;
    private String travelName;
    private String travelStartDate;
    private String travelEndDate;
    private String travelMainImage;
    private Boolean isDomesticTravel;
    private List<String> country;
    private int profileImage;

    public static TravelListResponse from(Travel travel, List<TravelCountry> travelCountries, int profileImage) {
        // TravelCountry 엔티티 목록에서 country 이름만 추출
        List<String> countryNames = travelCountries.stream()
            .map(travelCountry -> travelCountry.getCountry().name()) // 수정: TravelCountry에서 Country 객체 참조
            .collect(Collectors.toList());

        return new TravelListResponse(
            travel.getId(),
            travel.getTravelName(),
            travel.getTravelStartDate().toString(),
            travel.getTravelEndDate().toString(),
            travel.getMainImage(),
            travel.isDomesticTravel(),
            countryNames,
            profileImage
        );
    }
}
