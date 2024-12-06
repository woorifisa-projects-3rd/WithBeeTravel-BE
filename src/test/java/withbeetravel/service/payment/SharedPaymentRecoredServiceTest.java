package withbeetravel.service.payment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.domain.SharedPayment;
import withbeetravel.domain.Travel;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.ValidationErrorCode;
import withbeetravel.repository.SharedPaymentRepository;
import withbeetravel.repository.TravelRepository;
import withbeetravel.service.global.S3Uploader;
import withbeetravel.support.SharedPaymentFixture;
import withbeetravel.support.TravelFixture;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SharedPaymentRecoredServiceTest {

    @Mock private S3Uploader s3Uploader;
    @Mock private TravelRepository travelRepository;
    @Mock private SharedPaymentRepository sharedPaymentRepository;
    @Mock private MultipartFile image;

    @InjectMocks private SharedPaymentRecoredServiceImpl service;

    @Test
    void 이미지_업로드_및_메인_이미지_수정_성공() throws IOException {
        // Given
        Long travelId = 1L;
        Long sharedPaymentId = 1L;
        String newImageUrl = "https://s3.amazonaws.com/shared-payments/travel-1/image.png";
        String comment = "테스트";
        boolean isMainImage = true;

        Travel travel = TravelFixture.builder()
                .id(travelId)
                .build();

        SharedPayment sharedPayment = SharedPaymentFixture.builder()
                .id(sharedPaymentId)
                .travel(travel)
                .build();

        given(sharedPaymentRepository.findById(sharedPaymentId)).willReturn(Optional.of(sharedPayment));
        given(travelRepository.findById(travelId)).willReturn(Optional.of(travel));

        given(image.getSize()).willReturn(100L);

        // Mocking the upload method
        given(s3Uploader.upload(image, "shared-payments/" + travelId)).willReturn(newImageUrl);

        // When
        service.addAndUpdatePaymentRecord(travelId, sharedPaymentId, image, comment, isMainImage);

        // Then
        verify(sharedPaymentRepository).findById(sharedPaymentId);
        verify(travelRepository).findById(travelId);
        verify(s3Uploader).upload(image, "shared-payments/" + travelId);
        assertEquals(newImageUrl, sharedPayment.getPaymentImage());
        assertEquals(newImageUrl, travel.getMainImage());
        assertEquals(comment, sharedPayment.getPaymentComment());
    }

    @Test
    void 기존_이미지를_메인_이미지로_설정_성공() {
        // Given
        Long travelId = 1L;
        Long sharedPaymentId = 1L;
        String existingImageUrl = "https://s3.amazonaws.com/shared-payments/travel-1/old-image.png";
        String comment = "Updated payment comment";
        boolean isMainImage = true;

        Travel travel = TravelFixture.builder()
                .id(travelId)
                .build();

        SharedPayment sharedPayment = SharedPaymentFixture.builder()
                .id(sharedPaymentId)
                .travel(travel)
                .paymentImage(existingImageUrl)
                .build();

        given(sharedPaymentRepository.findById(sharedPaymentId)).willReturn(Optional.of(sharedPayment));
        given(travelRepository.findById(travelId)).willReturn(Optional.of(travel));

        // When
        service.addAndUpdatePaymentRecord(travelId, sharedPaymentId, null, comment, isMainImage);

        // Then
        verify(sharedPaymentRepository).findById(sharedPaymentId);
        verify(travelRepository).findById(travelId);
        assertEquals(existingImageUrl, travel.getMainImage());
        assertEquals(comment, sharedPayment.getPaymentComment());
    }

    @Test
    void 이미지_업로드_실패시_예외_발생() throws IOException {
        // Given
        Long travelId = 1L;
        Long sharedPaymentId = 1L;

        Travel travel = TravelFixture.builder()
                .id(travelId)
                .build();

        SharedPayment sharedPayment = SharedPaymentFixture.builder()
                .id(sharedPaymentId)
                .travel(travel)
                .build();

        given(sharedPaymentRepository.findById(sharedPaymentId)).willReturn(Optional.of(sharedPayment));
        given(travelRepository.findById(travelId)).willReturn(Optional.of(travel));
        given(image.getSize()).willReturn(100L);
        given(s3Uploader.upload(image, "shared-payments/" + travelId)).willThrow(IOException.class);

        // When & Then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> service.addAndUpdatePaymentRecord(travelId, sharedPaymentId, image, "Comment", false)
        );

        assertEquals(ValidationErrorCode.IMAGE_PROCESSING_FAILED, exception.getErrorCode());
        verify(sharedPaymentRepository).findById(sharedPaymentId);
        verify(travelRepository).findById(travelId);
    }
}