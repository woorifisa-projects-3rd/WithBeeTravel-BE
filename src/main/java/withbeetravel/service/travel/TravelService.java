package withbeetravel.service.travel;

import withbeetravel.dto.travel.TravelRequestDto;
import withbeetravel.dto.travel.TravelResponseDto;

public interface TravelService {
    TravelResponseDto saveTravel( TravelRequestDto request);
}
