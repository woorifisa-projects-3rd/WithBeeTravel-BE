package withbeetravel.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.SharedPayment;
import withbeetravel.dto.request.payment.SharedPaymentSearchRequest;
import withbeetravel.dto.response.payment.SharedPaymentListResponse;
import withbeetravel.dto.response.payment.SharedPaymentResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.PaymentErrorCode;
import withbeetravel.exception.error.ValidationErrorCode;
import withbeetravel.repository.SharedPaymentRepository;
import withbeetravel.repository.TravelMemberRepository;

import java.time.LocalDate;
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

    public Map<Long, List<String>> getParticipatingMembersMap(Page<SharedPayment> payments) {
        return payments.getContent().stream()
                .collect(Collectors.toMap(
                        SharedPayment::getId,
                        payment -> payment.getPaymentParticipatedMembers().stream()
                                .map(ppm -> ppm.getTravelMember().getUser().getProfileImage())
                                .collect(Collectors.toList())
                ));
    }
}
