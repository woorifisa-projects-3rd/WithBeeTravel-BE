package withbeetravel.service.travel;

import withbeetravel.dto.request.travel.InviteCodeSignUpRequest;
import withbeetravel.dto.request.travel.TravelRequest;
import withbeetravel.dto.response.travel.InviteCodeGetResponse;
import withbeetravel.dto.response.travel.InviteCodeSignUpResponse;
import withbeetravel.dto.response.travel.TravelResponse;

public interface
TravelService {
    TravelResponse saveTravel(TravelRequest request);

    void editTravel(TravelRequest request, Long travelId);

    InviteCodeSignUpResponse signUpTravel(InviteCodeSignUpRequest request);

    InviteCodeGetResponse getInviteCode(Long travelId);
}
