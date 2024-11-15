package withbeetravel.service.sharedPayment;

import withbeetravel.dto.request.sharedPayment.SharedPaymentParticipateRequest;
import withbeetravel.dto.response.SuccessResponse;

public interface SharedPaymentParticipantService {

    public SuccessResponse updateParticipantMembers(
            Long travelId,
            Long sharedPaymentId,
            SharedPaymentParticipateRequest sharedPaymentParticipateRequest
    );
}
