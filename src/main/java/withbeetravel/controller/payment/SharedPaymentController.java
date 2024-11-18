package withbeetravel.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.controller.docs.SharedPaymentControllerDocs;
import withbeetravel.dto.response.SharedPaymentResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.payment.SharedPaymentService;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels/{travelId}/payments")
public class SharedPaymentController implements SharedPaymentControllerDocs {

    private final SharedPaymentService sharedPaymentService;

    @CheckTravelAccess
    @GetMapping()
    public SuccessResponse<Page<SharedPaymentResponse>> getSharedPaymentAll(
            @PathVariable Long travelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "latest") String sortBy, // latest 또는 amount
            @RequestParam(required = false) Long memberId, // 특정 멤버 ID로 필터링
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        return sharedPaymentService.getSharedPaymentAll(travelId, page, sortBy, memberId, startDate, endDate);
    }
}
