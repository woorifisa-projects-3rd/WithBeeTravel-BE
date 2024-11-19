package withbeetravel.service.travel;

import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.TravelMemberResponse;

import java.util.List;

public interface TravelMemberService {

    SuccessResponse<List<TravelMemberResponse>> getTravelMembers(Long travelId);
}
