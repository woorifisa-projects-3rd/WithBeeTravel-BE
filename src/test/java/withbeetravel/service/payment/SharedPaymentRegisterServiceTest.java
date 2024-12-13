package withbeetravel.service.payment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.domain.*;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.ValidationErrorCode;
import withbeetravel.repository.*;
import withbeetravel.service.global.S3Uploader;
import withbeetravel.support.TravelFixture;
import withbeetravel.support.UserFixture;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SharedPaymentRegisterServiceTest {

    @Mock private TravelRepository travelRepository;
    @Mock private TravelMemberRepository travelMemberRepository;
    @Mock private SharedPaymentRepository sharedPaymentRepository;
    @Mock private PaymentParticipatedMemberRepository paymentParticipatedMemberRepository;
    @Mock private UserRepository userRepository;
    @Mock private HistoryRepository historyRepository;
    @Mock private TravelCountryRepository travelCountryRepository;
    @Mock private SharedPaymentCategoryClassificationService sharedPaymentCategoryClassificationService;
    @Mock private S3Uploader s3Uploader;
    @Mock private MultipartFile paymentImage;
    @Mock private MultipartFile image;

    @InjectMocks private SharedPaymentRegisterServiceImpl sharedPaymentRegisterService;

    @Test
    void 원화_결제_내역이_성공적으로_추가된다() {
        // Given
        Long userId = 1L;
        Long travelId = 1L;

        User user1 = UserFixture.builder().id(userId).build();
        User user2 = UserFixture.builder().id(2L).build();
        User user3 = UserFixture.builder().id(3L).build();

        Travel travel = TravelFixture.builder()
                .id(travelId)
                .build();

        TravelMember travelMember1 = TravelMember.builder()
                        .id(1L)
                        .travel(travel)
                        .user(user1)
                        .isCaptain(true)
                        .build();
        TravelMember travelMember2 = TravelMember.builder()
                .id(2L)
                .travel(travel)
                .user(user2)
                .isCaptain(false)
                .build();
        TravelMember travelMember3 = TravelMember.builder()
                .id(3L)
                .travel(travel)
                .user(user3)
                .isCaptain(false)
                .build();
        List<TravelMember> travelMembers = Arrays.asList(travelMember1, travelMember2, travelMember3);

        given(travelRepository.findById(travelId)).willReturn(Optional.of(travel));
        given(travelMemberRepository.findByTravelIdAndUserId(travelId, userId)).willReturn(Optional.of(travelMember1));
        given(travelMemberRepository.findAllByTravelId(travelId)).willReturn(travelMembers);

       // When
        sharedPaymentRegisterService.addManualSharedPayment(
                userId,
                travelId,
                "2024-12-06 20:04",
                "명수네 떡볶이",
                15000,
                null,
                "KRW",
                null,
                null,
                null,
                false
        );

        // Then
        verify(travelRepository).findById(travelId);
        verify(travelMemberRepository).findByTravelIdAndUserId(travelId, userId);
        verify(travelMemberRepository).findAllByTravelId(travelId);
        verify(sharedPaymentRepository).save(any(SharedPayment.class));
        verify(paymentParticipatedMemberRepository, times(3)).save(any(PaymentParticipatedMember.class));
    }

    @Test
    void 외화_결제_내역이_성공적으로_추가된다() {
        // Given
        Long userId = 1L;
        Long travelId = 1L;

        User user1 = UserFixture.builder().id(userId).build();
        User user2 = UserFixture.builder().id(2L).build();
        User user3 = UserFixture.builder().id(3L).build();

        Travel travel = TravelFixture.builder()
                .id(travelId)
                .build();

        TravelMember travelMember1 = TravelMember.builder()
                .id(1L)
                .travel(travel)
                .user(user1)
                .isCaptain(true)
                .build();
        TravelMember travelMember2 = TravelMember.builder()
                .id(2L)
                .travel(travel)
                .user(user2)
                .isCaptain(false)
                .build();
        TravelMember travelMember3 = TravelMember.builder()
                .id(3L)
                .travel(travel)
                .user(user3)
                .isCaptain(false)
                .build();
        List<TravelMember> travelMembers = Arrays.asList(travelMember1, travelMember2, travelMember3);

        given(travelRepository.findById(travelId)).willReturn(Optional.of(travel));
        given(travelMemberRepository.findByTravelIdAndUserId(travelId, userId)).willReturn(Optional.of(travelMember1));
        given(travelMemberRepository.findAllByTravelId(travelId)).willReturn(travelMembers);

        // When
        sharedPaymentRegisterService.addManualSharedPayment(
                userId,
                travelId,
                "2024-12-06 20:04",
                "명수네 떡볶이",
                14000,
                10.0,
                "USD",
                1400.0,
                null,
                null,
                false
        );

        // Then
        verify(travelRepository).findById(travelId);
        verify(travelMemberRepository).findByTravelIdAndUserId(travelId, userId);
        verify(travelMemberRepository).findAllByTravelId(travelId);
        verify(sharedPaymentRepository).save(any(SharedPayment.class));
        verify(paymentParticipatedMemberRepository, times(3)).save(any(PaymentParticipatedMember.class));
    }

    @Test
    void 외화_결제_내역_추가에_실패한다() {
        // Given
        Long userId = 1L;
        Long travelId = 1L;

        User user1 = UserFixture.builder().id(userId).build();
        User user2 = UserFixture.builder().id(2L).build();
        User user3 = UserFixture.builder().id(3L).build();

        Travel travel = TravelFixture.builder()
                .id(travelId)
                .build();

        TravelMember travelMember1 = TravelMember.builder()
                .id(1L)
                .travel(travel)
                .user(user1)
                .isCaptain(true)
                .build();
        TravelMember travelMember2 = TravelMember.builder()
                .id(2L)
                .travel(travel)
                .user(user2)
                .isCaptain(false)
                .build();
        TravelMember travelMember3 = TravelMember.builder()
                .id(3L)
                .travel(travel)
                .user(user3)
                .isCaptain(false)
                .build();
        List<TravelMember> travelMembers = Arrays.asList(travelMember1, travelMember2, travelMember3);

        given(travelRepository.findById(travelId)).willReturn(Optional.of(travel));
        given(travelMemberRepository.findByTravelIdAndUserId(travelId, userId)).willReturn(Optional.of(travelMember1));

        // When & Then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> sharedPaymentRegisterService.addManualSharedPayment(
                        userId,
                        travelId,
                        "2024-12-06 20:04",
                        "명수네 떡볶이",
                        14000,
                        10.0,
                        "USD",
                        null,
                        null,
                        null,
                        false
                )
        );

        assertEquals(ValidationErrorCode.MISSING_REQUIRED_FIELDS, exception.getErrorCode());

        verify(travelRepository).findById(travelId);
        verify(travelMemberRepository).findByTravelIdAndUserId(travelId, userId);
    }

    @Test
    void 사진을_포함한_결제_내역이_성공적으로_추가된다() throws IOException {
        // Given
        Long userId = 1L;
        Long travelId = 1L;
        String newImageUrl = "https://s3.amazonaws.com/shared-payments/travel-1/image.png";
        String comment = "테스트";

        User user1 = UserFixture.builder().id(userId).build();
        User user2 = UserFixture.builder().id(2L).build();
        User user3 = UserFixture.builder().id(3L).build();

        Travel travel = TravelFixture.builder()
                .id(travelId)
                .build();

        TravelMember travelMember1 = TravelMember.builder()
                .id(1L)
                .travel(travel)
                .user(user1)
                .isCaptain(true)
                .build();
        TravelMember travelMember2 = TravelMember.builder()
                .id(2L)
                .travel(travel)
                .user(user2)
                .isCaptain(false)
                .build();
        TravelMember travelMember3 = TravelMember.builder()
                .id(3L)
                .travel(travel)
                .user(user3)
                .isCaptain(false)
                .build();
        List<TravelMember> travelMembers = Arrays.asList(travelMember1, travelMember2, travelMember3);

        given(travelRepository.findById(travelId)).willReturn(Optional.of(travel));
        given(travelMemberRepository.findByTravelIdAndUserId(travelId, userId)).willReturn(Optional.of(travelMember1));
        given(image.getSize()).willReturn(100L);
        given(s3Uploader.upload(image, "shared-payments/" + travelId)).willReturn(newImageUrl);
        given(travelMemberRepository.findAllByTravelId(travelId)).willReturn(travelMembers);

        // When
        sharedPaymentRegisterService.addManualSharedPayment(
                userId,
                travelId,
                "2024-12-06 20:04",
                "명수네 떡볶이",
                15000,
                null,
                "KRW",
                null,
                image,
                comment,
                false
        );

        // Then
        verify(travelRepository).findById(travelId);
        verify(travelMemberRepository).findByTravelIdAndUserId(travelId, userId);
        verify(s3Uploader).upload(image, "shared-payments/" + travelId);
        verify(travelMemberRepository).findAllByTravelId(travelId);
        verify(sharedPaymentRepository).save(any(SharedPayment.class));
        verify(paymentParticipatedMemberRepository, times(3)).save(any(PaymentParticipatedMember.class));
    }

    @Test
    void 사진을_포함한_결제_내역_이밎_업로드_실패_시_예외_발생() throws IOException {
        // Given
        Long userId = 1L;
        Long travelId = 1L;
        String newImageUrl = "https://s3.amazonaws.com/shared-payments/travel-1/image.png";
        String comment = "테스트";

        User user1 = UserFixture.builder().id(userId).build();
        User user2 = UserFixture.builder().id(2L).build();
        User user3 = UserFixture.builder().id(3L).build();

        Travel travel = TravelFixture.builder()
                .id(travelId)
                .build();

        TravelMember travelMember1 = TravelMember.builder()
                .id(1L)
                .travel(travel)
                .user(user1)
                .isCaptain(true)
                .build();
        TravelMember travelMember2 = TravelMember.builder()
                .id(2L)
                .travel(travel)
                .user(user2)
                .isCaptain(false)
                .build();
        TravelMember travelMember3 = TravelMember.builder()
                .id(3L)
                .travel(travel)
                .user(user3)
                .isCaptain(false)
                .build();
        List<TravelMember> travelMembers = Arrays.asList(travelMember1, travelMember2, travelMember3);

        given(travelRepository.findById(travelId)).willReturn(Optional.of(travel));
        given(travelMemberRepository.findByTravelIdAndUserId(travelId, userId)).willReturn(Optional.of(travelMember1));
        given(image.getSize()).willReturn(100L);
        given(s3Uploader.upload(image, "shared-payments/" + travelId)).willThrow(IOException.class);

        // When & Then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> sharedPaymentRegisterService.addManualSharedPayment(
                        userId,
                        travelId,
                        "2024-12-06 20:04",
                        "명수네 떡볶이",
                        15000,
                        null,
                        "KRW",
                        null,
                        image,
                        comment,
                        false
                )
        );

        assertEquals(ValidationErrorCode.IMAGE_PROCESSING_FAILED, exception.getErrorCode());
        verify(travelRepository).findById(travelId);
        verify(travelMemberRepository).findByTravelIdAndUserId(travelId, userId);
        verify(s3Uploader).upload(image, "shared-payments/" + travelId);
    }

    @Test
    void 유효하지_않은_날짜_형식_입력_시_예외_발생() {
        // Given
        Long userId = 1L;
        Long travelId = 1L;

        User user1 = UserFixture.builder().id(userId).build();
        User user2 = UserFixture.builder().id(2L).build();
        User user3 = UserFixture.builder().id(3L).build();

        Travel travel = TravelFixture.builder()
                .id(travelId)
                .build();

        TravelMember travelMember1 = TravelMember.builder()
                .id(1L)
                .travel(travel)
                .user(user1)
                .isCaptain(true)
                .build();
        TravelMember travelMember2 = TravelMember.builder()
                .id(2L)
                .travel(travel)
                .user(user2)
                .isCaptain(false)
                .build();
        TravelMember travelMember3 = TravelMember.builder()
                .id(3L)
                .travel(travel)
                .user(user3)
                .isCaptain(false)
                .build();
        List<TravelMember> travelMembers = Arrays.asList(travelMember1, travelMember2, travelMember3);

        given(travelRepository.findById(travelId)).willReturn(Optional.of(travel));
        given(travelMemberRepository.findByTravelIdAndUserId(travelId, userId)).willReturn(Optional.of(travelMember1));

        // When & Then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> sharedPaymentRegisterService.addManualSharedPayment(
                        userId,
                        travelId,
                        "2024-12-06",
                        "명수네 떡볶이",
                        14000,
                        10.0,
                        "USD",
                        1400.0,
                        null,
                        null,
                        false
                )
        );

        assertEquals(ValidationErrorCode.INVALID_DATE_FORMAT, exception.getErrorCode());

        verify(travelRepository).findById(travelId);
        verify(travelMemberRepository).findByTravelIdAndUserId(travelId, userId);
        verify(travelMemberRepository).findAllByTravelId(travelId);
    }

    @Test
    void 지원되지_않는_통화_코드_입력_시_예외_발생() {
        // Given
        Long userId = 1L;
        Long travelId = 1L;

        User user1 = UserFixture.builder().id(userId).build();
        User user2 = UserFixture.builder().id(2L).build();
        User user3 = UserFixture.builder().id(3L).build();

        Travel travel = TravelFixture.builder()
                .id(travelId)
                .build();

        TravelMember travelMember1 = TravelMember.builder()
                .id(1L)
                .travel(travel)
                .user(user1)
                .isCaptain(true)
                .build();
        TravelMember travelMember2 = TravelMember.builder()
                .id(2L)
                .travel(travel)
                .user(user2)
                .isCaptain(false)
                .build();
        TravelMember travelMember3 = TravelMember.builder()
                .id(3L)
                .travel(travel)
                .user(user3)
                .isCaptain(false)
                .build();
        List<TravelMember> travelMembers = Arrays.asList(travelMember1, travelMember2, travelMember3);

        given(travelRepository.findById(travelId)).willReturn(Optional.of(travel));
        given(travelMemberRepository.findByTravelIdAndUserId(travelId, userId)).willReturn(Optional.of(travelMember1));

        // When & Then
        CustomException exception = assertThrows(
                CustomException.class,
                () -> sharedPaymentRegisterService.addManualSharedPayment(
                        userId,
                        travelId,
                        "2024-12-06 20:04",
                        "명수네 떡볶이",
                        14000,
                        10.0,
                        "USS",
                        1400.0,
                        null,
                        null,
                        false
                )
        );

        assertEquals(ValidationErrorCode.INVALID_CURRENCY_UNIT, exception.getErrorCode());

        verify(travelRepository).findById(travelId);
        verify(travelMemberRepository).findByTravelIdAndUserId(travelId, userId);
        verify(travelMemberRepository).findAllByTravelId(travelId);
    }
}