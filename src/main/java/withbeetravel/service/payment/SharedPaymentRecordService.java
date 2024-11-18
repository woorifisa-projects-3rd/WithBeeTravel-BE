package withbeetravel.service.payment;

import org.springframework.web.multipart.MultipartFile;
import withbeetravel.dto.response.payment.SharedPaymentRecordResponse;
import withbeetravel.dto.response.SuccessResponse;

public interface SharedPaymentRecordService {

    SuccessResponse addAndUpdatePaymentRecord(
            Long travelId,
            Long sharedPaymentId,
            MultipartFile image,
            String comment,
            boolean isMainImage
    );

    SuccessResponse<SharedPaymentRecordResponse> getSharedPaymentRecord(
            Long sharedPaymentId
    );
}
