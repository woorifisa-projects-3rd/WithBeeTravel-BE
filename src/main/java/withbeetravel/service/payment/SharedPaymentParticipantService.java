package withbeetravel.service.payment;

import withbeetravel.dto.request.payment.SharedPaymentParticipateRequest;
import withbeetravel.dto.response.SuccessResponse;

public interface SharedPaymentParticipantService {

    public void updateParticipantMembers(
            Long travelId,
            Long sharedPaymentId,
            SharedPaymentParticipateRequest sharedPaymentParticipateRequest
    );
}
