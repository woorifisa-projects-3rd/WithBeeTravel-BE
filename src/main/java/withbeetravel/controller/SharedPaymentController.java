package withbeetravel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.controller.docs.SharedPaymentControllerDocs;
import withbeetravel.dto.request.ChooseParticipantsRequestDto;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.dto.response.SharedPaymentRecordResponseDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.SharedPaymentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels/{travelId}/payments")
public class SharedPaymentController implements SharedPaymentControllerDocs {

    private final SharedPaymentService sharedPaymentService;

    @Override
    @PatchMapping("/{sharedPaymentId}/participants")
    public ResponseEntity<String> chooseParticipant(
            @PathVariable Long travelId,
            @PathVariable Long sharedPaymentId,
            @RequestBody ChooseParticipantsRequestDto requestDto
    ) {

        return ResponseEntity.ok("정산인원 변경 성공");
    }

    @Override
    @CheckTravelAccess
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
    @CheckTravelAccess
    @GetMapping("/{sharedPaymentId}/records")
    public SuccessResponse<SharedPaymentRecordResponseDto> getSharedPaymentRecord(
            @PathVariable Long travelId,
            @PathVariable Long sharedPaymentId
    ) {
        return sharedPaymentService.getSharedPaymentRecord(sharedPaymentId);
    }
}
