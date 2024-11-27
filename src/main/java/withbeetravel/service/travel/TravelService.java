package withbeetravel.service.travel;

import withbeetravel.dto.request.account.CardCompletedRequest;
import withbeetravel.dto.response.account.AccountConnectedWibeeResponse;
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
    TravelResponse saveTravel(TravelRequest request,Long userId);

    void editTravel(TravelRequest request, Long travelId);

    TravelHomeResponse getTravel(Long travelId);

    InviteCodeSignUpResponse signUpTravel(InviteCodeSignUpRequest request,Long userId);

    InviteCodeGetResponse getInviteCode(Long travelId);

    List<TravelListResponse> getTravelList(Long userId);

    void postConnectedAccount(CardCompletedRequest request, Long userId);

    AccountConnectedWibeeResponse getConnectedWibee(Long userId);
}
