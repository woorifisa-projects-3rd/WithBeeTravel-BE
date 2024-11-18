package withbeetravel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.aspect.CheckTravelAndSharedPaymentAccess;
import withbeetravel.controller.docs.SharedPaymentControllerDocs;
import withbeetravel.dto.request.ChooseParticipantsRequest;
import withbeetravel.dto.response.SharedPaymentRecordResponse;
import withbeetravel.dto.response.SharedPaymentResponse;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.SharedPaymentService;

import java.util.List;

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
            @RequestBody ChooseParticipantsRequest requestDto
    ) {

        return ResponseEntity.ok("정산인원 변경 성공");
    }

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

    @CheckTravelAccess
    @GetMapping()
    public SuccessResponse<Page<SharedPaymentResponse>> getSharedPaymentAll(
            @PathVariable Long travelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "latest") String sortBy  // latest 또는 amount
    ) {
        return sharedPaymentService.getSharedPaymentAll(travelId, page, sortBy);
    }
}
