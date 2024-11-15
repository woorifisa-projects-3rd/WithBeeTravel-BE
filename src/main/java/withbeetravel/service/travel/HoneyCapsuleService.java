package withbeetravel.service.travel;

import withbeetravel.dto.response.HoneyCapsuleListResponse;
import withbeetravel.dto.response.SuccessResponse;

public interface HoneyCapsuleService {

    SuccessResponse<HoneyCapsuleListResponse> getHoneyCapsuleList(Long travelId);
}
