package withbeetravel.dto.travel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import withbeetravel.domain.Travel;

import java.util.List;

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

    // Travel 엔터티나 특정 객체를 기반으로 Dto로 변환하는 from 메서드
    public static TravelResponseDto from(Travel travel) {
        return new TravelResponseDto(
                travel.getTravelName(),
                travel.getCountry(),          // Travel 엔티티의 필드에 맞게 변경 필요
                travel.getTravelStartDate().toString(),
                travel.getTravelEndDate().toString()
        );
    }
}
