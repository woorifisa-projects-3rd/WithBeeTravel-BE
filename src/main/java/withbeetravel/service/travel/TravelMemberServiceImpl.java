package withbeetravel.service.travel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.TravelMember;
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
    public List<TravelMember> getTravelMembers(Long travelId) {
        return travelMemberRepository.findAllByTravelId(travelId);
    }
}
