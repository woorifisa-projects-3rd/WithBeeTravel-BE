package withbeetravel.service.travel;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import withbeetravel.domain.*;
import withbeetravel.dto.request.account.CardCompletedRequest;
import withbeetravel.dto.response.account.AccountConnectedWibeeResponse;
import withbeetravel.dto.response.travel.TravelHomeResponse;
import withbeetravel.dto.request.travel.InviteCodeSignUpRequest;
import withbeetravel.dto.request.travel.TravelRequest;
import withbeetravel.dto.response.travel.InviteCodeGetResponse;
import withbeetravel.dto.response.travel.InviteCodeSignUpResponse;
import withbeetravel.dto.response.travel.TravelResponse;
import withbeetravel.dto.response.travel.TravelListResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.exception.error.BankingErrorCode;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.repository.AccountRepository;
import withbeetravel.repository.TravelCountryRepository;
import withbeetravel.repository.TravelRepository;
import withbeetravel.repository.*;
import withbeetravel.repository.notification.EmitterRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelServiceImpl implements TravelService {

    private static final Logger log = LoggerFactory.getLogger(TravelServiceImpl.class);
    private final TravelRepository travelRepository;
    private final TravelCountryRepository travelCountryRepository;
    private final AccountRepository accountRepository;
    private final SharedPaymentRepository sharedPaymentRepository;
    private final TravelMemberRepository travelMemberRepository;
    private final UserRepository userRepository;
    private final PaymentParticipatedMemberRepository paymentParticipatedMemberRepository;
    private final SettlementRequestLogRepository settlementRequestLogRepository;
    private final EmitterRepository emitterRepository;

    @Override
    public TravelResponse saveTravel(TravelRequest requestDto,Long userId) {

        List<Account> accounts = accountRepository.findByUserId(userId);
        System.out.println(accounts);
        boolean hasConnectedWibeeCard = accounts.stream()
            .anyMatch(Account::isConnectedWibeeCard);
        System.out.println(hasConnectedWibeeCard);
        if(!hasConnectedWibeeCard){
            throw new CustomException(TravelErrorCode.TRAVEL_CAPTAIN_NOT);
        }

        // 초대 코드 생성
        String inviteCode = UUID.randomUUID().toString();



        // Travel 엔티티 생성
        Travel travel = Travel.builder()
            .travelName(requestDto.getTravelName())
            .travelStartDate(LocalDate.parse(requestDto.getTravelStartDate()))
            .travelEndDate(LocalDate.parse(requestDto.getTravelEndDate()))
            .isDomesticTravel(requestDto.isDomesticTravel())
            .settlementStatus(SettlementStatus.PENDING)
            .inviteCode(inviteCode)
            .mainImage(null)
            .build();

        System.out.println("여행 데이터" + travel);
        Travel savedTravel = travelRepository.save(travel);  // Travel 엔티티 저장

        // TravelMember 엔티티 생성 (생성자를 Travel의 Captain으로 추가)
        TravelMember travelMember = TravelMember.builder()
            .travel(savedTravel)
            .user(userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND))) // 유저 검증
            .isCaptain(true) // Captain 역할로 지정
            .build();

        travelMemberRepository.save(travelMember); // TravelMember 엔티티 저장

        // 해외 여행일 경우, 선택된 나라들에 대해 유효성 검증 후 TravelCountry 엔티티 생성
        // TravelCountry 리스트를 빈 리스트로 초기화
        List<TravelCountry> travelCountries = List.of();
        if (!requestDto.isDomesticTravel()) {
            travelCountries = requestDto.getTravelCountries().stream()
                .map(countryName -> {
                    // Country enum에 존재하는지 검증
                    Country country = Country.findByName(countryName);
                    return TravelCountry.builder()
                        .country(country)
                        .travel(savedTravel)
                        .build();
                })
                .collect(Collectors.toList());

            // TravelCountry 엔티티 저장
            travelCountryRepository.saveAll(travelCountries);
        }

        // ResponseDto 생성 및 반환
        TravelResponse travelResponseDto = TravelResponse.from(savedTravel, travelCountries);

        return travelResponseDto;
    }

    @Override
    public void editTravel(TravelRequest requestDto, Long travelId){
        Travel travel = travelRepository.findById(travelId)
            .orElseThrow(() -> new IllegalArgumentException("Travel not found with ID : " + travelId));

        travel.updateTravel(requestDto.getTravelName(),
            LocalDate.parse(requestDto.getTravelStartDate()),
            LocalDate.parse(requestDto.getTravelEndDate()),
            requestDto.isDomesticTravel());

        travelCountryRepository.deleteByTravel(travel);

        if(!requestDto.isDomesticTravel()){

            List<TravelCountry> updatedTravelCountries = requestDto.getTravelCountries().stream()
                .map(countryName -> {
                    Country country = Country.findByName(countryName);
                    return  TravelCountry.builder().country(country).travel(travel).build();
                }).toList();

            travelCountryRepository.saveAll(updatedTravelCountries);
        }
    }

    @Override
    public InviteCodeSignUpResponse signUpTravel(InviteCodeSignUpRequest requestDto,Long userId){
        String inviteCode = requestDto.getInviteCode();

        Travel travel = travelRepository.findByInviteCode(inviteCode).
            orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_INVITECODE_NOT));

        Long travelId = travel.getId();
        int curMemberCount = travelMemberRepository.countByTravelId(travelId);


        if(curMemberCount >= 10){
            throw new CustomException(TravelErrorCode.TRAVEL_MEMBER_LIMIT);
        }

        boolean userAlreadyMember = travelMemberRepository.existsByTravelIdAndUserId(travelId, userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));

        if (userAlreadyMember) {
            throw new CustomException(TravelErrorCode.TRAVEL_USER_ALREADY_MEMBER);
        }

        // 기존의 여행 멤버 리스트
        List<TravelMember> travelMembers = travelMemberRepository.findAllByTravelId(travelId);

        TravelMember newMember = TravelMember.builder()
            .travel(travel)
            .user(user)
            .isCaptain(false)       // 초대한 사람은 Captain이 아님
            .build();


        travelMemberRepository.save(newMember);

        // 현재 공동지출내역 참여 멤버로 추가
        List<SharedPayment> sharedPayments = sharedPaymentRepository.findAllByTravelId(travelId);
        // PaymentParticipatedMember가 연관관계의 주인
        List<PaymentParticipatedMember> participatedMembers = sharedPayments.stream()
            .map(sharedPayment -> {
                // 참여자 수 증가
                sharedPayment.updateParticipantCount(sharedPayment.getParticipantCount() + 1);

                return PaymentParticipatedMember.builder()
                    .travelMember(newMember)
                    .sharedPayment(sharedPayment)
                    .build();
            })
            .collect(Collectors.toList());

        // PaymentParticipatedMember 저장
        paymentParticipatedMemberRepository.saveAll(participatedMembers);

        // 기존의 여행 멤버에게 알림 전송
        travelMembers.forEach(travelMember ->
            saveLog(travel, travelMember.getUser(), user, LogTitle.TRAVEL_MEMBER_ADDED));

        return InviteCodeSignUpResponse.builder()
            .travelId(travelId)
            .build();
    }


    private SettlementRequestLog saveLog(Travel travel, User user, User addedUser,
                                         LogTitle logTitle) {

        String logMessage = logTitle.getMessage(addedUser.getName(), travel.getTravelName());
        String link = logTitle.getLinkPattern(travel.getId());

        SettlementRequestLog log = settlementRequestLogRepository.save(
            SettlementRequestLog.builder()
                .travel(travel)
                .user(user)
                .logTitle(logTitle)
                .logMessage(logMessage)
                .link(link)
                .build());

        sendNotification(log);

        return log;
    }

    private void sendNotification(SettlementRequestLog settlementRequestLog) {
        String userId = String.valueOf(settlementRequestLog.getUser().getId());

        // 수신자에 연결된 모든 SseEmitter 객체를 가져옴
        Map<String, SseEmitter> emitters =
            emitterRepository.findAllEmitterStartWithByUserId(userId);

        // eventId 생성
        String eventId = userId + "_" + System.currentTimeMillis();

        // emitter를 순환하며 각 SseEmitter 객체에 알림 전송
        emitters.forEach(
            (key, sseEmitter) -> {
                Map<String, String> eventData = new HashMap<>();
                eventData.put("title", settlementRequestLog.getLogTitle().getTitle()); // 로그 타이틀 (ex. 정산 요청)
                eventData.put("message", settlementRequestLog.getLogMessage()); // 로그 메시지
                eventData.put("link", settlementRequestLog.getLink()); // 이동 링크
                emitterRepository.saveEventCache(key, eventData);
                try {
                    sseEmitter.send(SseEmitter.event().id(eventId).name("message").data(eventData));
                } catch (IOException e) {
                    emitterRepository.deleteById(key);
                }
            }
        );
    }

    @Override
    public TravelHomeResponse getTravel(Long travelId, Long userId) {
        // Aspect에서 이미 검증했으므로 Travel은 반드시 존재
        Travel travel = travelRepository.findById(travelId).get();
        TravelMember travelMember = travelMemberRepository.findByTravelIdAndUserId(travelId, userId)
            .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_ACCESS_FORBIDDEN));
        Map<String, Double> statistics = calculateStatistics(travelId);
        return TravelHomeResponse.of(travel, travelMember, statistics);
    }

    private Map<String, Double> calculateStatistics(Long travelId) {
        // 지출 데이터 조회
        List<SharedPayment> expenses = sharedPaymentRepository.findAllByTravelId(travelId);

        // 총 지출액 계산
        double totalAmount = expenses.stream()
            .mapToDouble(SharedPayment::getPaymentAmount)
            .sum();

        // 카테고리별 비율 계산
        return expenses.stream()
            .collect(Collectors.groupingBy(
                payment -> payment.getCategory().getDescription(),
                Collectors.collectingAndThen(
                    Collectors.summingDouble(SharedPayment::getPaymentAmount),
                    amount -> Math.round((amount / totalAmount) * 1000.0) / 10.0  // 소수점 첫째자리까지 반올림
                )
            ));
    }


    @Override
    public InviteCodeGetResponse getInviteCode(Long travelId){
        Travel travel = travelRepository.findById(travelId).orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_NOT_FOUND));

        return new InviteCodeGetResponse(travel.getInviteCode());
    }

    //   user의 여행 목록 리스트 조회
    @Override
    public List<TravelListResponse> getTravelList(Long userId) {

        // 여행 멤버 테이블에서 유저 id가 속한 여행 id 조회
        List<TravelMember> travelMembers = travelMemberRepository.findAllByUserId(userId);

        return travelMembers.stream()
            .map(travelMember -> {
                Travel travel = travelMember.getTravel();

                // 특정 여행에 속한 모든 멤버 조회
                List<TravelMember> members = travelMemberRepository.findAllByTravelId(travel.getId());

                // 캡틴 멤버 필터링 및 프로필 이미지 추출
                int profileImage = members.stream()
                    .filter(TravelMember::isCaptain)
                    .map(captain -> captain.getUser().getProfileImage())
                    .findFirst()
                    .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_CAPTAIN_NOT_FOUND));

                // 특정 여행에 속한 모든 국가 조회
                List<TravelCountry> travelCountries = travelCountryRepository.findByTravelId(travel.getId());

                // TravelListResponse 생성
                return TravelListResponse.from(travel, travelCountries, profileImage);
            }).toList();

    }


    //   연결한 계좌 및 위비 카드 발급 했는지 안했는 지 여부
    @Override
    public void postConnectedAccount(CardCompletedRequest request,Long userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));
        // 카드 발급 x , 연결계좌 0
        if(!request.isWibeeCard()){
            Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new CustomException(BankingErrorCode.ACCOUNT_NOT_FOUND));

            user.updateConnectedAccount(account);
        }

        // 카드 발급 0 , 연결계좌 0
        if (request.isWibeeCard()) {
            Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new CustomException(BankingErrorCode.ACCOUNT_NOT_FOUND));

            account.updatedAccount(request.isWibeeCard());
            user.updateConnectedAccount(account);
            user.updateWibeeCardAccount(account);
        }

    }


    @Override
    public AccountConnectedWibeeResponse getConnectedWibee(Long userId){
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));
        boolean isConnected = user.getWibeeCardAccount() != null;
        return new AccountConnectedWibeeResponse(isConnected);
    }


}
