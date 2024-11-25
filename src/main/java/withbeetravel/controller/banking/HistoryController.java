package withbeetravel.controller.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.dto.request.account.HistoryRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.banking.HistoryService;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/{accountId}/payment")
    public SuccessResponse addPayment(@PathVariable Long accountId,
                                      @RequestBody HistoryRequest historyRequest){

        historyService.addHistory(accountId,historyRequest);
        return SuccessResponse.of(
                HttpStatus.CREATED.value(),
                "결제 내역 등록 완료"
        );
    }

}
