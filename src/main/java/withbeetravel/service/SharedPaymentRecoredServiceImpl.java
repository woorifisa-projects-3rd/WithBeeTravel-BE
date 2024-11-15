package withbeetravel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.domain.SharedPayment;
import withbeetravel.domain.Travel;
import withbeetravel.dto.response.SharedPaymentRecordResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.PaymentErrorCode;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.exception.error.ValidationErrorCode;
import withbeetravel.repository.SharedPaymentRepository;
import withbeetravel.repository.TravelRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SharedPaymentRecoredServiceImpl implements SharedPaymentRecordService {

    private final S3Uploader s3Uploader;

    private final TravelRepository travelRepository;
    private final SharedPaymentRepository sharedPaymentRepository;

    // S3에 이미지를 저장할 경로
    private static final String SHARED_PAYMENT_IMAGE_DIR = "shared-payments";

    @Override
    @Transactional
    public SuccessResponse addAndUpdatePaymentRecord(
            Long travelId,
            Long sharedPaymentId,
            MultipartFile image,
            String comment,
            boolean isMainImage) {

        // SharedPayment Entity 가져오기
        SharedPayment sharedPayment = sharedPaymentRepository.findById(sharedPaymentId)
                .orElseThrow(() -> new CustomException(PaymentErrorCode.SHARED_PAYMENT_NOT_FOUND));

        // 이미지 추가, 수정, 삭제
        String newImage = uploadImage(travelId, sharedPayment, image);

        // 새로운 image 정보 엔티티에서 변경
        sharedPayment.updatePaymentImage(newImage);

        // comment 정보 엔티티에서 변경
        sharedPayment.updatePaymentCommnet(comment);

        // 메인 이미지로 설정해준 경우 여행 정보 수정
        if(isMainImage && image != null && newImage != null) {
            // 여행 정보 찾아오기
            Travel travel = travelRepository.findById(travelId)
                    .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_NOT_FOUND));

            // 여행 이미지 수정
            travel.updateMainImage(newImage);
        }

        return SuccessResponse.of(200, "이미지 및 문구를 성공적으로 변경하였습니다.");
    }

    @Override
    @Transactional(readOnly = true)
    public SuccessResponse<SharedPaymentRecordResponse> getSharedPaymentRecord(Long sharedPaymentId) {

        // SharedPayment 엔티티 가져오기
        SharedPayment sharedPayment = sharedPaymentRepository.findById(sharedPaymentId)
                .orElseThrow(() -> new CustomException(PaymentErrorCode.SHARED_PAYMENT_NOT_FOUND));

        // Response Dto에 담기
        SharedPaymentRecordResponse responseDto = SharedPaymentRecordResponse.from(sharedPayment);

        return SuccessResponse.of(200, "여행 기록 불러오기 성공", responseDto);
    }

    // 이미지 추가, 수정, 삭제
    private String uploadImage(Long travelId, SharedPayment sharedPayment, MultipartFile image) {

        // 원래 이미지
        String paymentImage = sharedPayment.getPaymentImage();

        // 새로 추가한 이미지
        String newImage = null;

        // image가 새로 들어왔다면 S3에 저장
        if(image != null) {

            // 이미지 저장할 S3 디렉토리 정보
            String dirName = SHARED_PAYMENT_IMAGE_DIR + "/" + travelId;

            try {
                if(paymentImage != null) { // 해당 공동결제 내역에 이미 이미지가 있다면 업데이트
                    newImage = s3Uploader.update(image, paymentImage, dirName);
                } else { // 없다면 업로드
                    newImage = s3Uploader.upload(image, dirName);
                }
            } catch (IOException e) { // 이미지 저장에 실패했을 경우
                throw new CustomException(ValidationErrorCode.IMAGE_PROCESSING_FAILED);
            }
        }
        // image가 null로 들어왔다면, 기존 이미지 삭제
        else {
            // S3에 이미지 삭제
            s3Uploader.delete(paymentImage);
        }

        // 새로운 이미지 정보 반환
        return newImage;
    }
}
