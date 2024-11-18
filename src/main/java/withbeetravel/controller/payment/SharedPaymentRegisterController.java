package withbeetravel.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.aspect.CheckTravelAndSharedPaymentAccess;
import withbeetravel.controller.payment.docs.SharedPaymentRegisterControllerDocs;
import withbeetravel.dto.request.payment.SharedPaymentWibeeCardRegisterRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.payment.SharedPaymentRegisterService;

@RestController
@RequestMapping("/api/travels/{travelId}/payments")
@RequiredArgsConstructor
public class SharedPaymentRegisterController implements SharedPaymentRegisterControllerDocs {

    private final SharedPaymentRegisterService sharedPaymentRegisterService;

    // TODO: userId 수정
    private static final Long userId = 1L;

    @Override
    @CheckTravelAccess
    @PostMapping(value = "/manual", consumes = "multipart/form-data")
    public SuccessResponse<Void> addManualSharedPayment(
            @PathVariable Long travelId,
            @RequestParam(value = "paymentDate") String paymentDate,
            @RequestParam(value = "storeName") String storeName,
            @RequestParam(value = "paymentAmount") int paymentAmount,
            @RequestParam(value = "foreignPaymentAmount", required = false) Double foreignPaymentAmount,
            @RequestParam(value = "currencyUnit")String currencyUnit,
            @RequestParam(value = "exchangeRate", required = false) Double exchangeRate,
            @RequestPart(value = "paymentImage") MultipartFile paymentImage,
            @RequestParam(value = "paymentComment", required = false) String paymentComment,
            @RequestParam(value = "isMainImage", defaultValue = "false") boolean isMainImage
    ) {
        
        return sharedPaymentRegisterService.addManualSharedPayment(
                userId, travelId, paymentDate, storeName, paymentAmount,
                foreignPaymentAmount, currencyUnit, exchangeRate, paymentImage, paymentComment,
                isMainImage
        );
    }

    @Override
    @CheckTravelAndSharedPaymentAccess
    @PatchMapping(value = "/{sharedPaymentId}", consumes = "multipart/form-data")
    public SuccessResponse<Void> updateManualSharedPayment(
            @PathVariable Long travelId,
            @PathVariable Long sharedPaymentId,
            @RequestParam(value = "paymentDate") String paymentDate,
            @RequestParam(value = "storeName") String storeName,
            @RequestParam(value = "paymentAmount") int paymentAmount,
            @RequestParam(value = "foreignPaymentAmount", required = false) Double foreignPaymentAmount,
            @RequestParam(value = "currencyUnit")String currencyUnit,
            @RequestParam(value = "exchangeRate", required = false) Double exchangeRate,
            @RequestPart(value = "paymentImage") MultipartFile paymentImage,
            @RequestParam(value = "paymentComment", required = false) String paymentComment,
            @RequestParam(value = "isMainImage", defaultValue = "false") boolean isMainImage
    ) {
        return sharedPaymentRegisterService.updateManualSharedPayment(
                userId, travelId, sharedPaymentId, paymentDate, storeName,
                paymentAmount, foreignPaymentAmount, currencyUnit, exchangeRate, paymentImage,
                paymentComment, isMainImage
        );
    }

    @Override
    @CheckTravelAccess
    @PostMapping("/manual-wibee-card")
    public SuccessResponse<Void> addWibeeCardSharedPayment(
            @PathVariable Long travelId,
            @RequestBody SharedPaymentWibeeCardRegisterRequest sharedPaymentWibeeCardRegisterRequest
    ) {
        return sharedPaymentRegisterService.addWibeeCardSharedPayment(
                userId, travelId, sharedPaymentWibeeCardRegisterRequest
        );
    }
}
