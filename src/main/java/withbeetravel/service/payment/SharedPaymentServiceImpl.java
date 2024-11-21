package withbeetravel.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.SharedPayment;
import withbeetravel.dto.request.payment.SharedPaymentSearchRequest;
import withbeetravel.repository.SharedPaymentRepository;
import withbeetravel.repository.TravelMemberRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedPaymentServiceImpl implements SharedPaymentService {

    private final SharedPaymentRepository sharedPaymentRepository;
    private final TravelMemberRepository travelMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<SharedPayment> getSharedPayments(Long travelId, SharedPaymentSearchRequest condition) {
        return sharedPaymentRepository.findAllByTravelIdAndMemberIdAndDateRange(
                travelId,
                condition.getMemberId(),
                condition.getStartDate(),
                condition.getEndDate(),
                PageRequest.of(condition.getPage(), 10,
                        Sort.by(Sort.Direction.DESC,  condition.getSortBy().equals("amount") ? "paymentAmount" : "paymentDate"))
        );
    }

    @Override
    public Map<Long, List<Integer>> getParticipatingMembersMap(Page<SharedPayment> payments) {
        return payments.getContent().stream()
                .collect(Collectors.toMap(
                        SharedPayment::getId,
                        payment -> payment.getPaymentParticipatedMembers().stream()
                                .map(ppm -> ppm.getTravelMember().getUser().getProfileImage())
                                .collect(Collectors.toList())
                ));
    }
}
