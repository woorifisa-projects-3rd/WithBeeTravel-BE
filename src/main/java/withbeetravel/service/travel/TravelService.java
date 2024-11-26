package withbeetravel.service.travel;

import withbeetravel.dto.response.travel.TravelHomeResponse;
import withbeetravel.dto.request.travel.InviteCodeSignUpRequest;
import withbeetravel.dto.request.travel.TravelRequest;
import withbeetravel.dto.response.travel.InviteCodeGetResponse;
import withbeetravel.dto.response.travel.InviteCodeSignUpResponse;
import withbeetravel.dto.response.travel.TravelResponse;
import withbeetravel.dto.response.travel.TravelListResponse;

import java.util.List;

public interface
TravelService {
    TravelResponse saveTravel(TravelRequest request);

    void editTravel(TravelRequest request, Long travelId);

    TravelHomeResponse getTravel(Long travelId);

    InviteCodeSignUpResponse signUpTravel(InviteCodeSignUpRequest request);

    InviteCodeGetResponse getInviteCode(Long travelId);

    List<TravelListResponse> getTravelList();
}
