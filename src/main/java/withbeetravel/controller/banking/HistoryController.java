package withbeetravel.controller.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckTravelAccess;
import withbeetravel.controller.banking.docs.HistoryControllerDocs;
import withbeetravel.dto.request.account.HistoryRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.account.WibeeCardHistoryListResponse;
import withbeetravel.security.UserAuthorizationUtil;
import withbeetravel.service.banking.HistoryService;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class HistoryController implements HistoryControllerDocs {

    private final HistoryService historyService;

    @PostMapping("/{accountId}/payment")
    public SuccessResponse<Void> addPayment(@PathVariable Long accountId,
                                      @RequestBody HistoryRequest historyRequest){

        historyService.addHistory(accountId,historyRequest);
        return SuccessResponse.of(
                HttpStatus.CREATED.value(),
                "결제 내역 등록 완료"
        );
    }

    @Override
    @GetMapping("/wibeeCardHistory")
    public SuccessResponse<WibeeCardHistoryListResponse> getWibeeCardHistory(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        Long userId = UserAuthorizationUtil.getLoginUserId();

        WibeeCardHistoryListResponse response = historyService.getWibeeCardHistory(userId, startDate, endDate);

        return SuccessResponse.of(HttpStatus.OK.value(), "위비 카드 결제 내역입니다.", response);
    }
}
