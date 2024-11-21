package withbeetravel.dto.request.travel;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TravelRequestDto {

    private String travelName;
    
    private boolean isDomesticTravel;

    private List<String> travelCountries;

    private String travelStartDate;

    private String travelEndDate;

    public TravelRequestDto(String travelName, Boolean isDomesticTravel, List<String> travelCountries, String travelStartDate, String travelEndDate) {
        this.travelName = travelName;
        this.isDomesticTravel = isDomesticTravel;
        this.travelCountries = travelCountries;
        this.travelStartDate = travelStartDate;
        this.travelEndDate = travelEndDate;
    }

}
