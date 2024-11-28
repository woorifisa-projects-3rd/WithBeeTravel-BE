package withbeetravel.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.aspect.CheckTravelAndSharedPaymentAccess;
import withbeetravel.controller.payment.docs.SharedPaymentRecordControllerDocs;
import withbeetravel.dto.response.payment.SharedPaymentRecordResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.payment.SharedPaymentRecordService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels/{travelId}/payments")
public class SharedPaymentRecordController implements SharedPaymentRecordControllerDocs {

    private final SharedPaymentRecordService sharedPaymentService;

    @Override
    @CheckTravelAndSharedPaymentAccess
    @PatchMapping(value = "/{sharedPaymentId}/records", consumes = "multipart/form-data")
    public SuccessResponse<Void> addAndUpdatePaymentRecord(
            @PathVariable Long travelId,
            @PathVariable Long sharedPaymentId,
            @RequestPart(value = "paymentImage") MultipartFile paymentImage,
            @RequestParam(value = "paymentComment", required = false) String paymentComment,
            @RequestParam(value = "isMainImage", defaultValue = "false") boolean isMainImage
    ) {

        sharedPaymentService.addAndUpdatePaymentRecord(travelId, sharedPaymentId, paymentImage, paymentComment, isMainImage)
        return SuccessResponse.of(HttpStatus.OK.value(), "이미지 및 문구를 성공적으로 변경하였습니다.");
    }

    @Override
    @CheckTravelAndSharedPaymentAccess
    @GetMapping("/{sharedPaymentId}/records")
    public SuccessResponse<SharedPaymentRecordResponse> getSharedPaymentRecord(
            @PathVariable Long travelId,
            @PathVariable Long sharedPaymentId
    ) {
        SharedPaymentRecordResponse response = sharedPaymentService.getSharedPaymentRecord(sharedPaymentId);
        return SuccessResponse.of(HttpStatus.OK.value(), "여행 기록 불러오기 성공", response);
    }
}
