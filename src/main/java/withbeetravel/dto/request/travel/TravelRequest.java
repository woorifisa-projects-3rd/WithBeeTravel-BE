package withbeetravel.dto.request.travel;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TravelRequest {

    private String travelName;
    
    private boolean isDomesticTravel;

    private List<String> travelCountries;

    private String travelStartDate;

    private String travelEndDate;

}
