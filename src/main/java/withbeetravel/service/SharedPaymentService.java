package withbeetravel.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.dto.response.SharedPaymentRecordResponse;
import withbeetravel.dto.response.SharedPaymentResponse;
import withbeetravel.dto.response.SuccessResponse;

import java.util.List;

public interface SharedPaymentService {

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

    SuccessResponse<Page<SharedPaymentResponse>> getSharedPaymentAll(Long travelId,
                                                                     int page,
                                                                     String sortBy);
}
