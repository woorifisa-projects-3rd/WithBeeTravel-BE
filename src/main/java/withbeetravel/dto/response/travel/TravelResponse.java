package withbeetravel.dto.response.travel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import withbeetravel.domain.Travel;
import withbeetravel.domain.TravelCountry;

import java.util.List;

@Getter
@Setter
public class TravelResponse {
    private Long travelId;
    private String name;
    private List<String> country;
    private String startDate;
    private String endDate;

    @Builder
    public TravelResponse(Long travelId, String name, List<String> country, String startDate, String endDate) {
        this.travelId = travelId;
        this.name = name;
        this.country = country;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Travel 엔터티를 기반으로 TravelResponseDto로 변환하는 from 메서드
    public static TravelResponse from(Travel travel, List<TravelCountry> travelCountries) {
        // TravelCountry 엔티티 목록에서 country 이름만 추출
        List<String> countryNames = travelCountries.stream()
                .map(travelCountry -> travelCountry.getCountry().name())
                .toList();

        return new TravelResponse(
                travel.getId(),
                travel.getTravelName(),
                countryNames,
                travel.getTravelStartDate().toString(),
                travel.getTravelEndDate().toString()
        );
    }
}
