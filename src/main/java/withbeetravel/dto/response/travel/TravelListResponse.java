package withbeetravel.dto.response.travel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TravelListResponse {
    private Long travelId;
    private String travelName;
    private String travelStartDate;
    private String travelEndDate;
    private String travelMainImage;
    private int profileImage;
}
