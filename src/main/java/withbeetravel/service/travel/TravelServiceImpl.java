package withbeetravel.service.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.Travel;
import withbeetravel.dto.travel.TravelRequestDto;
import withbeetravel.dto.travel.TravelResponseDto;
import withbeetravel.repository.travel.TravelRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TravelServiceImpl implements TravelService {

    private final TravelRepository travelRepository;

    @Override
    public TravelResponseDto saveTravel(Long travelId, TravelRequestDto requestDto) {
        if(!requestDto.isDomesticTravel()){
            List<String> countrys = requestDto.getCountry();
            if(C)
        }

        Travel.builder().travelName(requestDto.getName()).
                travelStartDate(LocalDate.parse(requestDto.getStartDate())) // String -> LocalDate 변환
                .travelEndDate(LocalDate.parse(requestDto.getEndDate())).isDomesticTravel(requestDto.isDomesticTravel()).build();
        Travel savedTravel = travelRepository.save(travel);  // 엔터티 저장
        return TravelResponseDto.from(savedTravel);  // 저장된 엔터티를 DTO로 변환하여 반환
    }
}
