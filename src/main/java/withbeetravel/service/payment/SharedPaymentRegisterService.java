package withbeetravel.service.payment;

import org.springframework.web.multipart.MultipartFile;
import withbeetravel.dto.request.payment.SharedPaymentWibeeCardRegisterRequest;
import withbeetravel.dto.response.SuccessResponse;

public interface SharedPaymentRegisterService {

    void addManualSharedPayment(
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

    void updateManualSharedPayment(
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

    void addWibeeCardSharedPayment(
            Long userId,
            Long travelId,
            SharedPaymentWibeeCardRegisterRequest sharedPaymentWibeeCardRegisterRequest
    );
}