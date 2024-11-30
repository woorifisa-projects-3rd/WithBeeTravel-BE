package withbeetravel.service.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelServiceImpl implements TravelService {

    private final TravelRepository travelRepository;
    private final TravelCountryRepository travelCountryRepository;
    private final AccountRepository accountRepository;
    private final SharedPaymentRepository sharedPaymentRepository;
    private final TravelMemberRepository travelMemberRepository;
    private final UserRepository userRepository;
    private final PaymentParticipatedMemberRepository paymentParticipatedMemberRepository;

    @Override
    public TravelResponse saveTravel(TravelRequest requestDto,Long userId) {

        List<Account> accounts = accountRepository.findByUserId(userId);
        boolean hasConnectedWibeeCard = accounts.stream()
                .anyMatch(Account::isConnectedWibeeCard);

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


        Travel savedTravel = travelRepository.save(travel);  // Travel 엔티티 저장

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

        return InviteCodeSignUpResponse.builder()
                .travelId(travelId)
                .build();
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

                    // TravelListResponse 생성
                    return new TravelListResponse(
                            travel.getId(),
                            travel.getTravelName(),
                            travel.getTravelStartDate().toString(),
                            travel.getTravelEndDate().toString(),
                            travel.getMainImage(),
                            profileImage
                    );
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
            System.out.println(request.isWibeeCard());
            account.updatedAccount(request.isWibeeCard());

            user.updateConnectedAccount(account);
            user.updateWibeeCardAccount(account);
        }

    }


    @Override
    public AccountConnectedWibeeResponse getConnectedWibee(Long userId){
        boolean isConnected = accountRepository.findById(userId)
                .map(Account::isConnectedWibeeCard) // Account 엔티티에 연결 여부 필드가 있다고 가정
                .orElseThrow(() -> new CustomException(BankingErrorCode.ACCOUNT_NOT_FOUND));

        return new AccountConnectedWibeeResponse(isConnected);
    }


}
