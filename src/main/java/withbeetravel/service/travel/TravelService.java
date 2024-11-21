package withbeetravel.service.travel;

import withbeetravel.dto.request.travel.TravelRequestDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.TravelListResponse;
import withbeetravel.dto.response.travel.TravelResponseDto;

import java.util.List;

public interface
TravelService {
    SuccessResponse<TravelResponseDto> saveTravel( TravelRequestDto request);

    SuccessResponse<Void> editTravel(TravelRequestDto request, Long travelId);

    List<TravelListResponse> getTravelList();
}
