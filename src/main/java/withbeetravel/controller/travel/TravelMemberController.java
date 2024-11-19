package withbeetravel.controller.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.TravelMemberResponse;
import withbeetravel.service.travel.TravelMemberService;

import java.util.List;

@RestController
@RequestMapping("/api/travels/{travelId}/members")
@RequiredArgsConstructor
public class TravelMemberController {

    private final TravelMemberService travelMemberService;

    @CheckTravelAccess
    @GetMapping
    public SuccessResponse<List<TravelMemberResponse>> getTravelMembers(@PathVariable Long travelId) {
        return travelMemberService.getTravelMembers(travelId);
    }
}
