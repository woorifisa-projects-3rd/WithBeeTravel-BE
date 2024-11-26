package withbeetravel.controller.payment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.aspect.PaymentValidation;
import withbeetravel.domain.SharedPayment;
import withbeetravel.domain.TravelMember;
import withbeetravel.dto.request.payment.SharedPaymentSearchRequest;
import withbeetravel.dto.response.ErrorResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.payment.SharedPaymentParticipatingMemberResponse;
import withbeetravel.dto.response.payment.SharedPaymentResponse;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.PaymentErrorCode;
import withbeetravel.service.payment.SharedPaymentService;
import withbeetravel.service.travel.TravelMemberService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels/{travelId}/payments")
public class SharedPaymentController {

    private final SharedPaymentService sharedPaymentService;

    @CheckTravelAccess
    @PaymentValidation
    @GetMapping
    public SuccessResponse<Page<SharedPaymentResponse>> getSharedPayments(
            @PathVariable Long travelId,
            @Valid @ModelAttribute SharedPaymentSearchRequest condition
    ) {
            Page<SharedPayment> payments = sharedPaymentService.getSharedPayments(travelId, condition);
            Map<Long, List<SharedPaymentParticipatingMemberResponse>> participatingMembersMap = sharedPaymentService.getParticipatingMembersMap(payments);

            return SuccessResponse.of(200, "모든 공동 결제 내역 조회 성공", SharedPaymentResponse.of(
                    payments,
                    payments.getContent().get(0).getTravel().getTravelMembers().size(),
                    participatingMembersMap
            ));
    }
}
