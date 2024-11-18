package withbeetravel.dto.response.travel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import withbeetravel.domain.Travel;
import withbeetravel.domain.TravelCountry;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TravelResponseDto {
    private String name;
    private List<String> country;
    private String startDate;
    private String endDate;

    @Builder
    public TravelResponseDto(String name, List<String> country, String startDate, String endDate) {
        this.name = name;
        this.country = country;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Travel 엔터티를 기반으로 TravelResponseDto로 변환하는 from 메서드
    public static TravelResponseDto from(Travel travel, List<TravelCountry> travelCountries) {
        // TravelCountry 엔티티 목록에서 country 이름만 추출
        List<String> countryNames = travelCountries.stream()
                .map(travelCountry -> travelCountry.getCountry().name())
                .toList();

        return new TravelResponseDto(
                travel.getTravelName(),
                countryNames,
                travel.getTravelStartDate().toString(),
                travel.getTravelEndDate().toString()
        );
    }
}
