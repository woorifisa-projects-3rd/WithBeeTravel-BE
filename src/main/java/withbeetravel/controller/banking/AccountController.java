package withbeetravel.controller.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import withbeetravel.domain.Account;
import withbeetravel.domain.History;
import withbeetravel.dto.banking.account.*;
import withbeetravel.service.banking.AccountService;
import withbeetravel.service.banking.HistoryService;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final HistoryService historyService;

    private final Long userId = 1L;
    private final Long accountId = 1L;

    @GetMapping()
    public List<AccountResponse> showAllAccount(){
        return accountService.showAll(userId);
    }

    @GetMapping("/histories")
    public List<HistoryResponse> showAllHistories(){
        return historyService.showAll(accountId);
    }

    @PostMapping()
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest accountRequest){

        // 계좌 생성
        Account createdAccount = accountService.createAccount(userId, accountRequest);

        // 생성된 계좌를 AccountResponse로 변환
        AccountResponse accountResponse = AccountResponse.from(createdAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponse);
    }

    @PostMapping("/transfer")
    public ResponseEntity transfer(@RequestBody TransferRequest transferRequest){

        accountService.transfer(transferRequest.getAccountId(),
                transferRequest.getAccountNumber(),
                transferRequest.getAmount(),
                transferRequest.getRqspeNm());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("송금이 완료되었습니다.");
    }

    @PostMapping("/deposit")
    public ResponseEntity deposit(@RequestBody DepositRequest depositRequest){

        accountService.deposit(accountId,depositRequest.getAmount(), depositRequest.getRqspeNm());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("입금이 완료되었습니다.");

    }

}
