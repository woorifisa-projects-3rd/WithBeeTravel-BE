package withbeetravel.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.Category;
import withbeetravel.domain.SharedPayment;
import withbeetravel.dto.request.payment.SharedPaymentSearchRequest;
import withbeetravel.dto.response.payment.SharedPaymentParticipatingMemberResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.PaymentErrorCode;
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
        Category category = null;
        try {
            if (condition.getCategory() != null && !condition.getCategory().isBlank()) {
                category = Category.fromString(condition.getCategory());
            }
        } catch (Error e) {
            throw new CustomException(PaymentErrorCode.SHARED_PAYMENT_NOT_FOUND);
        }

        Page<SharedPayment> payments = sharedPaymentRepository.findAllByTravelIdAndMemberIdAndDateRange(
                travelId,
                condition.getMemberId(),
                condition.getStartDate(),
                condition.getEndDate(),
                category,
                PageRequest.of(condition.getPage(), 10,
                        Sort.by(Sort.Direction.DESC, condition.getSortBy().equals("amount") ? "paymentAmount" : "paymentDate"))
        );

        if (payments.isEmpty()) {
            throw new CustomException(PaymentErrorCode.SHARED_PAYMENT_NOT_FOUND);
        }

        return payments;
    }

    @Override
    public Map<Long, List<SharedPaymentParticipatingMemberResponse>> getParticipatingMembersMap(Page<SharedPayment> payments) {
        return payments.getContent().stream()
                .collect(Collectors.toMap(
                        SharedPayment::getId,
                        payment -> payment.getPaymentParticipatedMembers().stream()
                                .map(ppm -> SharedPaymentParticipatingMemberResponse.builder()
                                        .id(ppm.getTravelMember().getId())
                                        .profileImage(ppm.getTravelMember().getUser().getProfileImage())
                                        .build())
                                .toList()
                ));
    }
}
