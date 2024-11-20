package withbeetravel.dto.request.travel;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TravelRequestDto {

    private String travelName;
    
    private boolean isDomesticTravel;

    private List<String> travelCountries;

    private String travelStartDate;

    private String travelEndDate;

}
