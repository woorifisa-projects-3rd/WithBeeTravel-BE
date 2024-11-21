package withbeetravel.controller.payment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.domain.SharedPayment;
import withbeetravel.domain.TravelMember;
import withbeetravel.dto.request.payment.SharedPaymentSearchRequest;
import withbeetravel.dto.response.payment.SharedPaymentListResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.payment.SharedPaymentService;
import withbeetravel.service.travel.TravelMemberService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels/{travelId}/payments")
public class SharedPaymentController {

    private final SharedPaymentService sharedPaymentService;
    private final TravelMemberService travelMemberService;

    @CheckTravelAccess
    @GetMapping
    public SuccessResponse<SharedPaymentListResponse> getSharedPayments(
            @PathVariable Long travelId,
            @Valid @ModelAttribute SharedPaymentSearchRequest condition
    ) {
        Page<SharedPayment> payments = sharedPaymentService.getSharedPayments(travelId, condition);
        List<TravelMember> travelMembers = travelMemberService.getTravelMembers(travelId);
        Map<Long, List<Integer>> participatingMembersMap = sharedPaymentService.getParticipatingMembersMap(payments);

        return SuccessResponse.of(200, "모든 공동 결제 내역 조회 성공", SharedPaymentListResponse.of(
                payments,
                travelMembers,
                travelMembers.size(),
                participatingMembersMap
        ));
    }
}
