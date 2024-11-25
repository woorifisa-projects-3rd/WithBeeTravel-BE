package withbeetravel.service.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.*;
import withbeetravel.dto.request.travel.InviteCodeSignUpRequest;
import withbeetravel.dto.request.travel.TravelRequest;
import withbeetravel.dto.response.travel.InviteCodeGetResponse;
import withbeetravel.dto.response.travel.InviteCodeSignUpResponse;
import withbeetravel.dto.response.travel.TravelResponse;
import withbeetravel.dto.response.travel.TravelListResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelServiceImpl implements TravelService {

    private final Long userId = 1L;

    private final TravelRepository travelRepository;
    private final TravelCountryRepository travelCountryRepository;
    private final AccountRepository accountRepository;
    private final TravelMemberRepository travelMemberRepository;
    private final UserRepository userRepository;

    @Override
    public TravelResponse saveTravel(TravelRequest requestDto) {

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
    public InviteCodeSignUpResponse signUpTravel(InviteCodeSignUpRequest requestDto){
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

        return InviteCodeSignUpResponse.builder()
                .travelId(travelId)
                .build();
    }


    @Override
    public InviteCodeGetResponse getInviteCode(Long travelId){
        Travel travel = travelRepository.findById(travelId).orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_NOT_FOUND));

        return new InviteCodeGetResponse(travel.getInviteCode());
    }
    
//   user의 여행 목록 리스트 조회
    @Override
    public List<TravelListResponse> getTravelList() {

        // 여행 멤버 테이블에서 유저 id가 속한 여행 id 조회   
        List<TravelMember> travelMembers = travelMemberRepository.findAllByUserId(userId);

        return travelMembers.stream()
                .map(travelMember -> {
                    Travel travel = travelMember.getTravel();

                    // 특정 여행에 속한 모든 멤버 조회
                    List<TravelMember> members = travelMemberRepository.findByTravelId(travel.getId());

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


}
