package withbeetravel.service.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.*;
import withbeetravel.dto.request.travel.TravelRequestDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.TravelHomeResponse;
import withbeetravel.dto.response.travel.TravelResponseDto;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.repository.AccountRepository;
import withbeetravel.repository.SharedPaymentRepository;
import withbeetravel.repository.TravelCountryRepository;
import withbeetravel.repository.TravelRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
    private final SharedPaymentRepository sharedPaymentRepository;

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

        travelCountryRepository.deleteByTravel(travel);

        if(!requestDto.isDomesticTravel()){

            List<TravelCountry> updatedTravelCountries = requestDto.getTravelCountries().stream()
                    .map(countryName -> {
                        Country country = Country.findByName(countryName);
                        return  TravelCountry.builder().country(country).travel(travel).build();
                    }).toList();

            travelCountryRepository.saveAll(updatedTravelCountries);
        }



        return  SuccessResponse.of(HttpStatus.OK.value(), "여행 정보를 성공적으로 변경");
    }

    @Override
    public TravelHomeResponse getTravel(Long travelId) {
        // Aspect에서 이미 검증했으므로 Travel은 반드시 존재
        Travel travel = travelRepository.findById(travelId).get();
        Map<String, Double> statistics = calculateStatistics(travelId);
        return TravelHomeResponse.of(travel, statistics);
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

}
