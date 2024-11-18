package withbeetravel.service.payment;

import org.springframework.web.multipart.MultipartFile;
import withbeetravel.dto.response.SuccessResponse;

public interface SharedPaymentRegisterService {

    SuccessResponse<Void> addManualSharedPayment(
            Long userId,
            Long travelId,
            String paymentDate,
            String storeName,
            int paymentAmount,
            Double foreignPaymentAmount,
            String currencyUnit,
            Double exchangeRate,
            MultipartFile paymentImage,
            String paymentComment,
            boolean isMainImage
    );

    SuccessResponse<Void> updateManualSharedPayment(
            Long userId,
            Long travelId,
            Long sharedPaymentId,
            String paymentDate,
            String storeName,
            int paymentAmount,
            Double foreignPaymentAmount,
            String currencyUnit,
            Double exchangeRate,
            MultipartFile paymentImage,
            String paymentComment,
            boolean isMainImage
    );
}
