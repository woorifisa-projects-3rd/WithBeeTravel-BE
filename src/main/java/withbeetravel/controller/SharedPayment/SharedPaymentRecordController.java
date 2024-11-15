package withbeetravel.controller.SharedPayment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.aspect.CheckTravelAndSharedPaymentAccess;
import withbeetravel.controller.SharedPayment.docs.SharedPaymentRecordControllerDocs;
import withbeetravel.dto.response.SharedPaymentRecordResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.SharedPaymentRecordService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels/{travelId}/payments")
public class SharedPaymentRecordController implements SharedPaymentRecordControllerDocs {

    private final SharedPaymentRecordService sharedPaymentService;

    @Override
    @CheckTravelAndSharedPaymentAccess
    @PatchMapping(value = "/{sharedPaymentId}/records", consumes = "multipart/form-data")
    public SuccessResponse addAndUpdatePaymentRecord(
            @PathVariable Long travelId,
            @PathVariable Long sharedPaymentId,
            @RequestPart(value = "paymentImage") MultipartFile paymentImage,
            @RequestParam(value = "paymentComment", required = false) String paymentComment,
            @RequestParam(value = "isMainImage", defaultValue = "false") boolean isMainImage
    ) {

        return sharedPaymentService.addAndUpdatePaymentRecord(travelId, sharedPaymentId, paymentImage, paymentComment, isMainImage);
    }

    @Override
    @CheckTravelAndSharedPaymentAccess
    @GetMapping("/{sharedPaymentId}/records")
    public SuccessResponse<SharedPaymentRecordResponse> getSharedPaymentRecord(
            @PathVariable Long travelId,
            @PathVariable Long sharedPaymentId
    ) {
        return sharedPaymentService.getSharedPaymentRecord(sharedPaymentId);
    }
}
