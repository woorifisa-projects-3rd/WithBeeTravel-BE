package withbeetravel.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.aspect.CheckTravelAndSharedPaymentAccess;
import withbeetravel.controller.payment.docs.SharedPaymentRegisterControllerDocs;
import withbeetravel.dto.request.payment.SharedPaymentWibeeCardRegisterRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.payment.CurrencyUnitResponse;
import withbeetravel.security.UserAuthorizationUtil;
import withbeetravel.service.payment.SharedPaymentRegisterService;

@RestController
@RequestMapping("/api/travels/{travelId}/payments")
@RequiredArgsConstructor
public class SharedPaymentRegisterController implements SharedPaymentRegisterControllerDocs {

    private final SharedPaymentRegisterService sharedPaymentRegisterService;

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

        Long userId = UserAuthorizationUtil.getLoginUserId();

        sharedPaymentRegisterService.addManualSharedPayment(
                userId, travelId, paymentDate, storeName, paymentAmount,
                foreignPaymentAmount, currencyUnit, exchangeRate, paymentImage, paymentComment,
                isMainImage
        );

        return SuccessResponse.of(HttpStatus.OK.value(), "결제 내역이 추가되었습니다.");
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
        Long userId = UserAuthorizationUtil.getLoginUserId();

        sharedPaymentRegisterService.updateManualSharedPayment(
                userId, travelId, sharedPaymentId, paymentDate, storeName,
                paymentAmount, foreignPaymentAmount, currencyUnit, exchangeRate, paymentImage,
                paymentComment, isMainImage
        );

        return SuccessResponse.of(HttpStatus.OK.value(), "결제 내역 정보가 수정되었습니다.");
    }

    @Override
    @CheckTravelAccess
    @PostMapping("/manual-wibee-card")
    public SuccessResponse<Void> addWibeeCardSharedPayment(
            @PathVariable Long travelId,
            @RequestBody SharedPaymentWibeeCardRegisterRequest sharedPaymentWibeeCardRegisterRequest
    ) {

        Long userId = UserAuthorizationUtil.getLoginUserId();

        sharedPaymentRegisterService.addWibeeCardSharedPayment(
                userId, travelId, sharedPaymentWibeeCardRegisterRequest
        );

        return SuccessResponse.of(HttpStatus.OK.value(), "결제 내역이 추가되었습니다.");
    }

    @Override
    @CheckTravelAccess
    @GetMapping("/currency-unit")
    public SuccessResponse<CurrencyUnitResponse> getCurrencyUnitOptions(
            @PathVariable Long travelId
    ) {

        CurrencyUnitResponse response = sharedPaymentRegisterService.getCurrencyUnitOptions(travelId);

        return SuccessResponse.of(HttpStatus.OK.value(), "통화 코드 목록입니다.", response);
    }
}
