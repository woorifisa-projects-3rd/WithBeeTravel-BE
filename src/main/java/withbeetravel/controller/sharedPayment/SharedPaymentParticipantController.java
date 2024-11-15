package withbeetravel.controller.sharedPayment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAndSharedPaymentAccess;
import withbeetravel.controller.sharedPayment.docs.SharedPaymentParticipantControllerDocs;
import withbeetravel.dto.request.sharedPayment.SharedPaymentParticipateRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.sharedPayment.SharedPaymentParticipantService;

@RestController
@RequestMapping("/api/travels/{travelId}/payments/{sharedPaymentId}/participants")
@RequiredArgsConstructor
public class SharedPaymentParticipantController implements SharedPaymentParticipantControllerDocs {

    private final SharedPaymentParticipantService sharedPaymentParticipantService;

    @Override
    @PatchMapping
    @CheckTravelAndSharedPaymentAccess
    public SuccessResponse updateParticipantMembers(
            @PathVariable Long travelId,
            @PathVariable Long sharedPaymentId,
            @RequestBody SharedPaymentParticipateRequest sharedPaymentParticipateRequest
    ) {

        return sharedPaymentParticipantService
                .updateParticipantMembers(travelId, sharedPaymentId, sharedPaymentParticipateRequest);
    }
}
