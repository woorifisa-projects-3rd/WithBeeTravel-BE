package withbeetravel.service.travel;

import withbeetravel.dto.request.travel.TravelRequestDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.TravelResponseDto;

public interface
TravelService {
    TravelResponseDto saveTravel( TravelRequestDto request);

    void editTravel(TravelRequestDto request, Long travelId);
}
