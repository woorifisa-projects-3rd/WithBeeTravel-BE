package withbeetravel.controller.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.controller.travel.docs.HoneyCapsuleControllerDocs;
import withbeetravel.dto.response.travel.HoneyCapsuleResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.travel.HoneyCapsuleService;

import java.util.List;

@RestController
@RequestMapping("/api/travels/{travelId}/honeycapsule")
@RequiredArgsConstructor
public class HoneyCapsuleController implements HoneyCapsuleControllerDocs {

    private final HoneyCapsuleService honeyCapsuleService;

    @Override
    @CheckTravelAccess
    @GetMapping
    public SuccessResponse<List<HoneyCapsuleResponse>> getHoneyCapsuleList(
            @PathVariable Long travelId
    ) {

        return honeyCapsuleService.getHoneyCapsuleList(travelId);
    }

}
