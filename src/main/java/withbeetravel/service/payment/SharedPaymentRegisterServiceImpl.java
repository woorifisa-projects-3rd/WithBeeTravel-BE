package withbeetravel.service.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.config.OpenAIConfig;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    // 생성형 AI 관련 필드
    private final OpenAIConfig config;
    private final ObjectMapper objectMapper;
    private final OkHttpClient client = new OkHttpClient();

    // 생성형 AI 메시지 클래스
    @RequiredArgsConstructor
    @Getter
    private static class Message {
        private final String role;
        private final String content;
    }

    @Override
    @Transactional
    public void addManualSharedPayment(
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
    }

    @Override
    @Transactional
    public void updateManualSharedPayment(
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
    }

    @Override
    @Transactional
    public void addWibeeCardSharedPayment(
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
    public Category getCategory(String storeName) {
        try {
            List<Message> messages = new ArrayList<>();
            messages.add(new Message("system",
                    "당신은 여행 카테고리를 분류하는 AI입니다. " +
                            "상호명을 보고 다음 카테고리 중 하나로만 분류해주세요. 국가는 한국입니다: " +
                            "항공, 교통, 숙박, 식비, 관광, 액티비티, 쇼핑, 기타 " +
                            "카테고리 이름만 정확히 답변해주세요. " +
                            "- 호텔, 리조트, 에어비앤비 등은 '숙박' " +
                            "- 식당, 카페, 바 등은 '식비' " +
                            "- 버스, 택시, 지하철, 기차 등은 '교통' " +
                            "- 항공사, 공항 등은 '항공' " +
                            "- 박물관, 미술관, 랜드마크 등은 '관광' " +
                            "- 테마파크, 스포츠, 체험 등은 '액티비티' " +
                            "- 마트, 쇼핑몰, 아울렛 등은 '쇼핑' 으로 분류해주세요."
            ));

            messages.add(new Message("user", String.format("상호명: %s", storeName)));

            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "model", config.getModel(),
                    "messages", messages,
                    "temperature", 0.3
            ));

            Request request = new Request.Builder()
                    .url(config.getEndpoint())
                    .post(RequestBody.create(
                            requestBody,
                            MediaType.parse("application/json")
                    ))
                    .addHeader("Authorization", "Bearer " + config.getApiKey())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new CustomException(TravelErrorCode.TRAVEL_CATEGORY_NOT_FOUND);
                }

                JsonNode jsonResponse = objectMapper.readTree(response.body().string());
                String categoryName = jsonResponse
                        .path("choices")
                        .get(0)
                        .path("message")
                        .path("content")
                        .asText()
                        .trim();

                // 반환된 카테고리명을 Category enum으로 변환
                return switch (categoryName) {
                    case "항공" -> Category.FLIGHT;
                    case "교통" -> Category.TRANSPORTATION;
                    case "숙박" -> Category.ACCOMMODATION;
                    case "식비" -> Category.FOOD;
                    case "관광" -> Category.TOUR;
                    case "액티비티" -> Category.ACTIVITY;
                    case "쇼핑" -> Category.SHOPPING;
                    default -> Category.ETC;
                };
            }
        } catch (Exception e) {
            // OpenAI API 호출 실패시 기타로 분류
            return Category.ETC;
        }
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

        // 여행일 이전이나 이후에 발생한 결제 내역이면 검사 통과
        if (travel.getTravelStartDate().isAfter(history.getDate().toLocalDate())
                || travel.getTravelEndDate().isBefore(history.getDate().toLocalDate()))
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
