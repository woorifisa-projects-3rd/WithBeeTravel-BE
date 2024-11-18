package withbeetravel.service.travel;

import withbeetravel.dto.response.travel.HoneyCapsuleResponse;
import withbeetravel.dto.response.SuccessResponse;

import java.util.List;

public interface HoneyCapsuleService {

    SuccessResponse<List<HoneyCapsuleResponse>> getHoneyCapsuleList(Long travelId);
}
