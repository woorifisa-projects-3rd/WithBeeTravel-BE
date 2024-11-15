package withbeetravel.service;

import org.springframework.web.multipart.MultipartFile;
import withbeetravel.dto.response.SharedPaymentRecordResponse;
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
