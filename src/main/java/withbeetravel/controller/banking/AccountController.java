package withbeetravel.controller.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import withbeetravel.domain.Account;
import withbeetravel.domain.History;
import withbeetravel.dto.banking.account.*;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.service.banking.AccountService;
import withbeetravel.service.banking.HistoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final HistoryService historyService;

    private final Long userId = 1L;
    //private final Long accountId = 1L;

    @GetMapping()
    public List<AccountResponse> showAllAccount(){
        return accountService.showAll(userId);
    }

    @GetMapping("/{accountId}/info")
    public AccountResponse accountInfo(@PathVariable Long accountId){
        return accountService.accountInfo(accountId);
    }

    @GetMapping("/{accountId}")
    public List<HistoryResponse> showAllHistories(@PathVariable Long accountId){
        return historyService.showAll(accountId);
    }

    @PostMapping()
    public SuccessResponse<AccountResponse> createAccount(@RequestBody AccountRequest accountRequest){

        // 계좌 생성
        return  accountService.createAccount(userId, accountRequest);
    }

    @PostMapping("{accountId}/transfer")
    public ResponseEntity<SuccessResponse<String>> transfer(@RequestBody TransferRequest transferRequest){

        accountService.transfer(transferRequest.getAccountId(),
                transferRequest.getAccountNumber(),
                transferRequest.getAmount(),
                transferRequest.getRqspeNm());

        SuccessResponse response = SuccessResponse.of(
                HttpStatus.ACCEPTED.value(),
                "송금이 완료되었습니다."

        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PostMapping("{accountId}/deposit")
    public ResponseEntity<SuccessResponse<String>> deposit(
            @RequestBody DepositRequest depositRequest,
            @PathVariable Long accountId) {

        // 입금 처리
        accountService.deposit(accountId, depositRequest.getAmount(), depositRequest.getRqspeNm());

        // SuccessResponse 객체를 생성하여 반환
        SuccessResponse<String> response = SuccessResponse.of(
                HttpStatus.ACCEPTED.value(), // 상태 코드 202
                "입금이 완료되었습니다." // 메시지
        );

        // ResponseEntity로 감싸서 반환
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/verify/{accountNumber}")
    public ResponseEntity verifyAccount(@PathVariable String accountNumber){

        if(accountService.verifyAccount(accountNumber)){
            return ResponseEntity.ok("ok");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
    }

    @GetMapping("/find-user/{accountNumber}")
    public SuccessResponse<AccountOwnerNameResponse> findUserNameByAccountNumber(@PathVariable String accountNumber) {

        String name = accountService.findUserNameByAccountNumber(accountNumber);

        AccountOwnerNameResponse accountOwnerNameResponse = new AccountOwnerNameResponse(name);

        SuccessResponse<AccountOwnerNameResponse> response = SuccessResponse.of(
                HttpStatus.FOUND.value(),
                "찾은 계좌 주인 이름",
                accountOwnerNameResponse
        );

        // SuccessResponse 반환
        return response;
    }

}
