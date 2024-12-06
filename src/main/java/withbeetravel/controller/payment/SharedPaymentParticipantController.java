package withbeetravel.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAndSharedPaymentAccess;
import withbeetravel.controller.payment.docs.SharedPaymentParticipantControllerDocs;
import withbeetravel.dto.request.payment.SharedPaymentParticipateRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.payment.SharedPaymentParticipantService;

@RestController
@RequestMapping("/api/travels/{travelId}/payments/{sharedPaymentId}/participants")
@RequiredArgsConstructor
public class SharedPaymentParticipantController implements SharedPaymentParticipantControllerDocs {

    private final SharedPaymentParticipantService sharedPaymentParticipantService;

    @Override
    @PatchMapping
    @CheckTravelAndSharedPaymentAccess
    public SuccessResponse<Void> updateParticipantMembers(
            @PathVariable Long travelId,
            @PathVariable Long sharedPaymentId,
            @RequestBody SharedPaymentParticipateRequest sharedPaymentParticipateRequest
    ) {

        sharedPaymentParticipantService
                .updateParticipantMembers(travelId, sharedPaymentId, sharedPaymentParticipateRequest);

        return SuccessResponse.of(HttpStatus.OK.value(), "정산인원 변경 성공");
    }
}
