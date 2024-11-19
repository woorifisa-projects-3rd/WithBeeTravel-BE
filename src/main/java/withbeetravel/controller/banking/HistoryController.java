package withbeetravel.controller.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import withbeetravel.dto.request.account.HistoryRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.banking.HistoryService;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/{accountId}/payment")
    public SuccessResponse addPayment(@PathVariable Long accountId,
                                      @RequestBody HistoryRequest historyRequest){

        return historyService.addHistory(accountId, historyRequest);

    }

}
