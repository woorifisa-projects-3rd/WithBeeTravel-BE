package withbeetravel.service.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.Country;
import withbeetravel.domain.Travel;
import withbeetravel.domain.TravelCountry;
import withbeetravel.dto.travel.TravelRequestDto;
import withbeetravel.dto.travel.TravelResponseDto;
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
                .travelName(requestDto.getName())
                .travelStartDate(LocalDate.parse(requestDto.getStartDate()))
                .travelEndDate(LocalDate.parse(requestDto.getEndDate()))
                .isDomesticTravel(requestDto.isDomesticTravel())
                .settlementStatus(SettlementStatus.PENDING)
                .inviteCode(inviteCode)
                .mainImage(null)
                .build();


        Travel savedTravel = travelRepository.save(travel);  // Travel 엔티티 저장

        // 해외 여행일 경우, 선택된 나라들에 대해 유효성 검증 후 TravelCountry 엔티티 생성
        List<TravelCountry> travelCountries = null;
        if (!requestDto.isDomesticTravel()) {
            travelCountries = requestDto.getCountry().stream()
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
        return TravelResponseDto.from(savedTravel, travelCountries != null ? travelCountries : List.of());
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
