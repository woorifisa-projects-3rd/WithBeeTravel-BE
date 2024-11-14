package withbeetravel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.domain.SharedPayment;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.PaymentErrorCode;
import withbeetravel.exception.error.ValidationErrorCode;
import withbeetravel.repository.SharedPaymentRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SharedPaymentService {

    private final S3Uploader s3Uploader;

    private final SharedPaymentRepository sharedPaymentRepository;

    private static final String SHARED_PAYMENT_IMAGE_DIR = "shared-payments";

    public boolean addAndUpdatePaymentRecord(
            Long sharedPaymentId,
            MultipartFile image,
            String comment,
            boolean isMainImage) {

        // SharedPayment Entity 가져오기
        SharedPayment sharedPayment = sharedPaymentRepository.findById(sharedPaymentId)
                .orElseThrow(() -> new CustomException(PaymentErrorCode.SHARED_PAYMENT_NOT_FOUND));

        String paymentImage = sharedPayment.getPaymentImage();
        String newPaymentImage;
        try {
            if(paymentImage != null) { // 해당 공동결제 내역에 이미 이미지가 있다면 업데이트
                newPaymentImage = s3Uploader.update(image, paymentImage, SHARED_PAYMENT_IMAGE_DIR);
            } else { // 없다면 업로드
                newPaymentImage = s3Uploader.upload(image, SHARED_PAYMENT_IMAGE_DIR);
            }
        } catch (IOException e) { // 이미지 저장에 실패했을 경우
            throw new CustomException(ValidationErrorCode.IMAGE_PROCESSING_FAILED);
        }

        System.out.println(newPaymentImage);

        return false;
    }
}
