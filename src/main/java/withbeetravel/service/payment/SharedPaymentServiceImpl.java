package withbeetravel.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.SharedPayment;
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
    public Page<SharedPaymentResponse> getSharedPaymentAll(Long travelId,
                                                                            int page,
                                                                            String sortBy,
                                                                            Long memberId,
                                                                            LocalDate startDate,
                                                                            LocalDate endDate) {
        // 정렬 타입 검증
        if (!sortBy.equals("latest") && !sortBy.equals("amount")) {
            throw new CustomException(PaymentErrorCode.INVALID_SORT_TYPE);
        }

        // 날짜 범위 검증
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new CustomException(ValidationErrorCode.DATE_RANGE_ERROR);
        }

        // 멤버 ID가 제공된 경우, 해당 멤버가 이 여행의 멤버인지 확인
        if (memberId != null) {
            boolean isMemberOfTravel = travelMemberRepository.existsByTravelIdAndId(travelId, memberId);
            if (!isMemberOfTravel) {
                throw new CustomException(PaymentErrorCode.NON_TRAVEL_MEMBER_INCLUDED);
            }
        }

        // sortBy가 amount면 금액 내림차순, 아니면 날짜 내림차순
        Pageable pageable = PageRequest.of(page, 10,
                Sort.by(Sort.Direction.DESC, sortBy.equals("amount") ? "paymentAmount" : "paymentDate"));

        // 해당 여행의 공동 결제 내역 페이지 조회
        Page<SharedPayment> sharedPayments = sharedPaymentRepository.findAllByTravelIdAndMemberIdAndDateRange(
                travelId, memberId, startDate, endDate, pageable);

        if (sharedPayments.isEmpty()) {
            throw new CustomException(PaymentErrorCode.SHARED_PAYMENT_NOT_FOUND);
        }

        int totalTravelMembers = sharedPayments.getContent().get(0).getTravel().getTravelMembers().size();
        // 각 SharedPayment의 참여 멤버 Map 생성
        Map<Long, List<String>> participatingMembersMap = sharedPayments.getContent().stream()
                .collect(Collectors.toMap(
                        SharedPayment::getId,
                        payment -> payment.getPaymentParticipatedMembers()
                                .stream()
                                .map(member -> member.getTravelMember().getUser().getProfileImage())
                                .toList()
                ));

        return SharedPaymentResponse.of(sharedPayments, totalTravelMembers, participatingMembersMap);
    }
}
