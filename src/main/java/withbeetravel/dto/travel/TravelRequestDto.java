package withbeetravel.dto.travel;


import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class TravelRequestDto {

    private String name;
    private boolean isDomesticTravel;
    private List<String> country;
    private String startDate;
    private String endDate;

    public TravelRequestDto(String name, Boolean isDomesticTravel, List<String> country, String startDate, String endDate) {
        this.name = name;
        this.isDomesticTravel = isDomesticTravel;
        this.country = country;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
