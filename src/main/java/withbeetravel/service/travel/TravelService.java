package withbeetravel.service.travel;

import withbeetravel.dto.request.TravelRequestDto;
import withbeetravel.dto.response.travel.TravelResponseDto;

public interface TravelService {
    TravelResponseDto saveTravel( TravelRequestDto request);
}
