package withbeetravel.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.domain.*;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.exception.error.ValidationErrorCode;
import withbeetravel.repository.PaymentParticipatedMemberRepository;
import withbeetravel.repository.SharedPaymentRepository;
import withbeetravel.repository.TravelMemberRepository;
import withbeetravel.repository.TravelRepository;
import withbeetravel.service.global.S3Uploader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SharedPaymentRegisterServiceImpl implements SharedPaymentRegisterService{

    private final TravelRepository travelRepository;
    private final TravelMemberRepository travelMemberRepository;
    private final SharedPaymentRepository sharedPaymentRepository;
    private final PaymentParticipatedMemberRepository paymentParticipatedMemberRepository;

    private final S3Uploader s3Uploader;

    // S3에 이미지를 저장할 경로
    private static final String SHARED_PAYMENT_IMAGE_DIR = "shared-payments/";

    @Override
    @Transactional
    public SuccessResponse<Void> addManualSharedPayment(
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
    ) {

        // travelId에 따른 여행 가져오기
        Travel travel = getTravel(travelId);

        // userId와 travelId에 따른 여행 멤버 가져오기
        TravelMember travelMember = getTravelMember(userId, travelId);

        // 외화 금액과 환율 정보가 하나라도 들어왔다면, 두 값이 모두 들어왔는지 확인
        validatePaymentAmount(foreignPaymentAmount, exchangeRate);

        // 이미지 값이 들어왔을 경우 S3에 업로드
        String imageUrl;
        try {
            imageUrl = s3Uploader.upload(paymentImage, SHARED_PAYMENT_IMAGE_DIR + travelId);
        } catch (IOException e) {
            throw new CustomException(ValidationErrorCode.IMAGE_PROCESSING_FAILED);
        }

        // 메인 이미지로 설정했다면, 여행 메인 사진 바꿔주기
        if(isMainImage) setTravelMainImage(travel, imageUrl);

        // 여행 멤버 리스트 가져오기
        List<TravelMember> members = getTravelMembers(travelId);

        // 새로운 결제 내역 추가
        SharedPayment sharedPayment = SharedPayment.builder()
                .addedByMember(travelMember)
                .travel(travel)
                .currencyUnit(CurrencyUnit.from(currencyUnit))
                .paymentAmount(paymentAmount)
                .foreignPaymentAmount(foreignPaymentAmount)
                .exchangeRate(exchangeRate)
                .paymentComment(paymentComment)
                .paymentImage(imageUrl)
                .isManuallyAdded(true)
                .participantCount(members.size())
                .category(getCategory(storeName))
                .storeName(storeName)
                .paymentDate(dateTimeFormatter(paymentDate))
                .build();
        sharedPaymentRepository.save(sharedPayment);

        // 공동 결제 내역 참여 멤버 수정
        setParticipatedMembers(sharedPayment, members);

        return SuccessResponse.of(HttpStatus.OK.value(), "결제 내역이 추가되었습니다.");
    }

    Travel getTravel(Long travelId) {
        return travelRepository.findById(travelId)
                .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_NOT_FOUND));
    }

    TravelMember getTravelMember(Long userId, Long travelId) {
        return travelMemberRepository.findByTravelIdAndUserId(travelId, userId)
                .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_ACCESS_FORBIDDEN));
    }

    void validatePaymentAmount(Double foreignPaymentAmount, Double exchangeRate) {

        // 둘 다 null 값이거나, null 값이 아니거나
        if((foreignPaymentAmount == null && exchangeRate == null) ||
                (foreignPaymentAmount != null && exchangeRate != null)) return;

        throw new CustomException(ValidationErrorCode.MISSING_REQUIRED_FIELDS);
    }

    List<TravelMember> getTravelMembers(Long travelId) {
        return travelMemberRepository.findAllByTravelId(travelId);
    }

    // TODO: 생성형 AI로 상호명으로 카테고리 구하기
    Category getCategory(String storeName) { // 상호명으로 카테고리 구하기

        return Category.ETC;
    }

    LocalDateTime dateTimeFormatter(String date) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new CustomException(ValidationErrorCode.INVALID_DATE_FORMAT);
        }

    }

    void setParticipatedMembers(SharedPayment sharedPayment, List<TravelMember> members) {
        for (TravelMember member : members) {
            paymentParticipatedMemberRepository.save(
                    PaymentParticipatedMember.builder()
                            .travelMember(member)
                            .sharedPayment(sharedPayment)
                            .build()
            );
        }
    }

    void setTravelMainImage(Travel travel, String imageUrl) {

        // 이미지가 없는데 메인 이미지로 설정한 경우
        if(imageUrl == null)
            throw new CustomException(ValidationErrorCode.MISSING_REQUIRED_FIELDS);

        travel.updateMainImage(imageUrl);
    }
}
