package withbeetravel.service.travel;

import withbeetravel.domain.TravelMember;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.TravelMemberResponse;

import java.util.List;

public interface TravelMemberService {

    List<TravelMember> getTravelMembers(Long travelId);
}
