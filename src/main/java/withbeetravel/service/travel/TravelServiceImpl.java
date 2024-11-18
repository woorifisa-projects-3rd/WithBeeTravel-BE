package withbeetravel.service.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.Country;
import withbeetravel.domain.Travel;
import withbeetravel.domain.TravelCountry;
import withbeetravel.dto.request.travel.TravelRequestDto;
import withbeetravel.dto.response.travel.TravelResponseDto;
import withbeetravel.repository.travel.TravelCountryRepository;
import withbeetravel.repository.travel.TravelRepository;
import withbeetravel.domain.SettlementStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelServiceImpl implements TravelService {

    private final TravelRepository travelRepository;
    private final TravelCountryRepository travelCountryRepository;

    @Override
    public TravelResponseDto saveTravel(TravelRequestDto requestDto) {
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
        List<TravelCountry> travelCountries = null;
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
        return TravelResponseDto.from( savedTravel, travelCountries != null ? travelCountries : List.of());
    }

    @Override
    public TravelResponseDto editTravel(TravelRequestDto requestDto, Long travelId){
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
                    }).collect(Collectors.toList());

            travelCountryRepository.saveAll(updatedTravelCountries);
        }

        Travel updatedTravel = travelRepository.save(travel);

        List<TravelCountry> travelCountries = travelCountryRepository.findByTravelId(updatedTravel.getId());

        return TravelResponseDto.from(updatedTravel, travelCountries);
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
