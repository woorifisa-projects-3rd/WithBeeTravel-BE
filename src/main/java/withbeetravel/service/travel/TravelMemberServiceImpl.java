package withbeetravel.service.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.travel.TravelMemberResponse;
import withbeetravel.repository.TravelMemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelMemberServiceImpl implements TravelMemberService {

    private final TravelMemberRepository travelMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public SuccessResponse<List<TravelMemberResponse>> getTravelMembers(Long travelId) {
        List<TravelMemberResponse> members = travelMemberRepository.findAllByTravelId(travelId)
                .stream()
                .map(TravelMemberResponse::from)  // 또는 TravelMemberResponse::from
                .collect(Collectors.toList());

        return SuccessResponse.of(200, "여행 멤버 조회 성공", members);
    }
}
