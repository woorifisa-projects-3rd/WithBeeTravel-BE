package withbeetravel.service.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.*;
import withbeetravel.dto.request.travel.TravelRequestDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.TravelResponseDto;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.repository.AccountRepository;
import withbeetravel.repository.TravelCountryRepository;
import withbeetravel.repository.TravelRepository;

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

    @Override
    public SuccessResponse<TravelResponseDto> saveTravel(TravelRequestDto requestDto) {

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
                        Country country = findCountryByName(countryName);
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
        TravelResponseDto travelResponseDto = TravelResponseDto.from(savedTravel, travelCountries);

        return SuccessResponse.of(HttpStatus.OK.value(), "여행 생성 성공",travelResponseDto);
    }

    @Override
    public SuccessResponse<Void> editTravel(TravelRequestDto requestDto, Long travelId){
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new IllegalArgumentException("Travel not found with ID : " + travelId));

        travel.updateTravel(requestDto.getTravelName(),
                LocalDate.parse(requestDto.getTravelStartDate()),
                LocalDate.parse(requestDto.getTravelEndDate()),
                requestDto.isDomesticTravel());

        if(!requestDto.isDomesticTravel()){

            travelCountryRepository.deleteByTravel(travel);

            List<TravelCountry> updatedTravelCountries = requestDto.getTravelCountries().stream()
                    .map(countryName -> {
                        Country country = findCountryByName(countryName);
                        return  TravelCountry.builder().country(country).travel(travel).build();
                    }).toList();

            travelCountryRepository.saveAll(updatedTravelCountries);
        }

        return  SuccessResponse.of(HttpStatus.OK.value(), "여행 정보를 성공적으로 변경");
    }

    // 나라 이름으로 Country enum 찾는 메서드
    private Country findCountryByName(String countryName) {
        String name = countryName.trim();

        return List.of(Country.values()).stream()
                .filter(country -> country.getCountryName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant for country name: " + countryName));
    }
}
