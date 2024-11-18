package withbeetravel.service.travel;

import withbeetravel.dto.request.travel.TravelRequestDto;
import withbeetravel.dto.response.travel.TravelResponseDto;

public interface TravelService {
    TravelResponseDto saveTravel( TravelRequestDto request);

    TravelResponseDto editTravel( TravelRequestDto request, Long travelId);
}
