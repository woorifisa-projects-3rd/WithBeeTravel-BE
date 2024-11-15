package withbeetravel.service;

import org.springframework.web.multipart.MultipartFile;
import withbeetravel.dto.response.SharedPaymentRecordResponseDto;
import withbeetravel.dto.response.SuccessResponse;

public interface SharedPaymentService {

    SuccessResponse addAndUpdatePaymentRecord(
            Long travelId,
            Long sharedPaymentId,
            MultipartFile image,
            String comment,
            boolean isMainImage
    );

    SuccessResponse<SharedPaymentRecordResponseDto> getSharedPaymentRecord(
            Long sharedPaymentId
    );
}
