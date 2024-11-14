package withbeetravel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import withbeetravel.controller.docs.SharedPaymentControllerDocs;
import withbeetravel.dto.ChooseParticipantsRequestDto;
import withbeetravel.aspect.CheckTravelAccess;
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
    public ResponseEntity<String> addAndUpdatePaymentRecord(
            @PathVariable Long travelId,
            @PathVariable Long sharedPaymentId,
            @RequestPart(value = "paymentImage") MultipartFile paymentImage,
            @RequestParam(value = "paymentComment", required = false) String paymentComment,
            @RequestParam(value = "isMainImage", defaultValue = "false") boolean isMainImage
    ) {

        sharedPaymentService.addAndUpdatePaymentRecord(sharedPaymentId, paymentImage, paymentComment, isMainImage);

        return null;
    }
}
