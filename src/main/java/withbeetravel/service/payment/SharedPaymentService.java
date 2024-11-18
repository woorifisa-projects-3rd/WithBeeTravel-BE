package withbeetravel.service.payment;

import org.springframework.data.domain.Page;
import withbeetravel.dto.response.SharedPaymentResponse;
import withbeetravel.dto.response.SuccessResponse;

import java.time.LocalDate;

public interface SharedPaymentService {
    SuccessResponse<Page<SharedPaymentResponse>> getSharedPaymentAll(Long travelId,
                                                                     int page,
                                                                     String sortBy,
                                                                     Long memberId,
                                                                     LocalDate startDate,
                                                                     LocalDate endDate);
}
