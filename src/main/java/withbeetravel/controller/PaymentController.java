package withbeetravel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import withbeetravel.controller.docs.PaymentControllerDocs;
import withbeetravel.dto.ChooseParticipantsRequestDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/travels/{travelId}/payments")
public class PaymentController implements PaymentControllerDocs {


    @PatchMapping("/{sharedPaymentId}/participants")
    public ResponseEntity<String> chooseParticipant(@PathVariable Long travelId,
                                                                @PathVariable Long sharedPaymentId,
                                                                @RequestBody ChooseParticipantsRequestDto requestDto) {

        return ResponseEntity.ok("정산인원 변경 성공");
    }
}
