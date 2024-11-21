package withbeetravel.service.payment;

import org.springframework.data.domain.Page;
import withbeetravel.domain.SharedPayment;
import withbeetravel.dto.request.payment.SharedPaymentSearchRequest;
import withbeetravel.dto.response.payment.SharedPaymentResponse;
import withbeetravel.dto.response.SuccessResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SharedPaymentService {
    Page<SharedPayment> getSharedPayments(Long travelId, SharedPaymentSearchRequest condition);

    public Map<Long, List<String>> getParticipatingMembersMap(Page<SharedPayment> payments);
}
