package withbeetravel.dto.response.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelAdminResponse {

    private Long travelId;

    private String travelName;

    private String travelType;

    private String travelStartDate;

    private String travelEndDate;

    private int totalMember;

    private Long captainId;

    private String settlementStatus;

}
