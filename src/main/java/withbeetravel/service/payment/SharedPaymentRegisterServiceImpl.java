package withbeetravel.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.domain.*;
import withbeetravel.dto.request.payment.SharedPaymentWibeeCardRegisterRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.*;
import withbeetravel.repository.*;
import withbeetravel.service.global.S3Uploader;

import java.io.IOException;
import java.time.LocalDate;
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
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

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

    @Override
    @Transactional
    public SuccessResponse<Void> updateManualSharedPayment(
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
    ) {

        // 공동 결제 내역 정보 가져오기
        SharedPayment sharedPayment = getSharedPayment(sharedPaymentId);

        // 여행 정보 가져오기
        Travel travel = getTravel(travelId);

        // 수정할 수 있는 공동 결제 내역인지 확인
        validateUpdateSharedPayment(userId, travelId, sharedPayment);

        // 외화 금액과 환율 정보가 하나라도 들어왔다면, 두 값이 모두 들어왔는지 확인
        validatePaymentAmount(foreignPaymentAmount, exchangeRate);

        // 이미지 값이 들어왔을 경우 S3에 업로드
        String imageUrl;
        try {
            imageUrl = s3Uploader.update(paymentImage, sharedPayment.getPaymentImage(), SHARED_PAYMENT_IMAGE_DIR + travelId);
        } catch (IOException e) {
            throw new CustomException(ValidationErrorCode.IMAGE_PROCESSING_FAILED);
        }

        // 메인 이미지로 설정했다면, 여행 메인 사진 바꿔주기
        if(isMainImage) setTravelMainImage(travel, imageUrl);

        // 공동 내역 수정
        sharedPayment.updateManuallyPayment(
                CurrencyUnit.from(currencyUnit),
                paymentAmount,
                foreignPaymentAmount,
                exchangeRate,
                paymentComment,
                imageUrl,
                getCategory(storeName),
                storeName,
                dateTimeFormatter(paymentDate)
        );


        return SuccessResponse.of(HttpStatus.OK.value(), "결제 내역 정보가 수정되었습니다.");
    }

    @Override
    @Transactional
    public SuccessResponse<Void> addWibeeCardSharedPayment(
            Long userId,
            Long travelId,
            SharedPaymentWibeeCardRegisterRequest sharedPaymentWibeeCardRegisterRequest
    ) {

        // 회원 정보 가져오기
        User user = getUser(userId);

        // 위비 트래블 카드가 연동되어 있는 계좌
        Account account = getConnectedWibeeCardAccount(user);

        // 여행 정보 가져오기
        Travel travel = getTravel(travelId);

        // 여행 멤버 가져오기
        TravelMember travelMember = getTravelMember(userId, travelId);

        // 입력으로 들어온 거래 내역을 공동 결제 내역으로 저장
        registerWibeeCardSharedPayment(
                sharedPaymentWibeeCardRegisterRequest.getHistoryId(),
                travel,
                travelMember,
                account
        );

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

    List<TravelMember> getTravelMembers(Long travelId) {
        return travelMemberRepository.findAllByTravelId(travelId);
    }

    SharedPayment getSharedPayment(Long sharedPaymentId) {
        return sharedPaymentRepository.findById(sharedPaymentId)
                .orElseThrow(() -> new CustomException(PaymentErrorCode.SHARED_PAYMENT_NOT_FOUND));
    }

    // TODO: 생성형 AI로 상호명으로 카테고리 구하기
    Category getCategory(String storeName) { // 상호명으로 카테고리 구하기

        return Category.ETC;
    }

    User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(AuthErrorCode.AUTHENTICATION_FAILED));
    }

    Account getConnectedWibeeCardAccount(User user) {

        // 위비 카드를 발급 받지 않은 회원인 경우
        if(user.getWibeeCardAccount() == null)
            throw new CustomException(BankingErrorCode.WIBEE_CARD_NOT_ISSUED);

        return user.getWibeeCardAccount();
    }

    History getHistory(Long historyId) {
        return historyRepository.findById(historyId)
                .orElseThrow(() -> new CustomException(BankingErrorCode.HISTORY_NOT_FOUND));
    }

    void validatePaymentAmount(Double foreignPaymentAmount, Double exchangeRate) {

        // 둘 다 null 값이거나, null 값이 아니거나
        if((foreignPaymentAmount == null && exchangeRate == null) ||
                (foreignPaymentAmount != null && exchangeRate != null)) return;

        throw new CustomException(ValidationErrorCode.MISSING_REQUIRED_FIELDS);
    }

    void validateUpdateSharedPayment(Long userId, Long travelId, SharedPayment sharedPayment) {

        // 직접 추가 결제 내역이 아닌 경우, 수정 불가
        if(!sharedPayment.isManuallyAdded())
            throw new CustomException(PaymentErrorCode.NO_PERMISSION_TO_MODIFY_SHARED_PAYMENT);

        // 로그인된 회원이 해당 결제 내역을 추가한 멤버가 아닌 경우, 수정 불가
        TravelMember travelMember = getTravelMember(userId, travelId);
        if(!sharedPayment.getAddedByMember().equals(travelMember))
            throw new CustomException(PaymentErrorCode.NO_PERMISSION_TO_MODIFY_SHARED_PAYMENT);
    }

    void validateHistoryToAccount(History history, Account account) {

        if(history.getAccount() != account)
            throw new CustomException(BankingErrorCode.HISTORY_ACCESS_FORBIDDEN);
    }

    void validateisWibeeCardUsedHistory(History history) {

        if(!history.isWibeeCard()) {
            throw new CustomException(BankingErrorCode.HISTORY_ACCESS_FORBIDDEN);
        }
    }

    void validateHistoryDate(Travel travel, History history) {

        System.out.println(travel.getTravelStartDate());
        System.out.println(history.getDate().toLocalDate());
        System.out.println(travel.getTravelEndDate());

        // 여행일 이전이나 이후에 발생한 결제 내역이면 검사 통과
        if (travel.getTravelStartDate().isBefore(history.getDate().toLocalDate())
                || travel.getTravelEndDate().isAfter(history.getDate().toLocalDate()))
            return;

        throw new CustomException(ValidationErrorCode.DATE_RANGE_ERROR);
    }

    void validateisAddedHistory(History history) {
        if(history.isAddedSharedPayment())
            throw new CustomException(BankingErrorCode.PAYMENT_ALREADY_EXISTS);
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

    void saveWibeeCardSharedPayment(
            TravelMember travelMember,
            Travel travel,
            History history
    ) {
        sharedPaymentRepository.save(
                SharedPayment.builder()
                        .addedByMember(travelMember)
                        .travel(travel)
                        .currencyUnit(CurrencyUnit.KRW)
                        .paymentAmount(history.getPayAM())
                        .isManuallyAdded(false)
                        .category(getCategory(history.getRqspeNm()))
                        .storeName(history.getRqspeNm())
                        .paymentDate(history.getDate())
                        .build()
        );
    }

    void registerWibeeCardSharedPayment(
            List<Long> historyId,
            Travel travel,
            TravelMember travelMember,
            Account account
    ) {
        for (Long id : historyId) {
            // 거래 내역 가져오기
            History history = getHistory(id);

            // 위비 카드에 연결된 계좌의 거래 내역이 맞는지 확인
            validateHistoryToAccount(history, account);

            // 위비 카드 결제 내역이 맞는지 확인
            validateisWibeeCardUsedHistory(history);

            // 해당 거래 내역이 여행 기간 전이나 후에 발생한 거래내역이 맞는지
            validateHistoryDate(travel, history);

            // 이미 가져온 거래 내역인지 확인
            validateisAddedHistory(history);

            // 공동 결제 내역에 추가
            saveWibeeCardSharedPayment(travelMember, travel, history);

            // 추가 후 상태 변경
            history.addedSharedPayment();
        }
    }
}
