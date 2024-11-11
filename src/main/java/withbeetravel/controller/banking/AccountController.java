package withbeetravel.controller.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import withbeetravel.domain.Account;
import withbeetravel.domain.History;
import withbeetravel.dto.banking.account.AccountRequest;
import withbeetravel.dto.banking.account.AccountResponse;
import withbeetravel.dto.banking.account.HistoryResponse;
import withbeetravel.service.banking.AccountService;
import withbeetravel.service.banking.HistoryService;

import java.util.List;

@RestController
@RequestMapping("/banking/{userId}")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final HistoryService historyService;

    @GetMapping("/account")
    public List<AccountResponse> showAllAccount(@PathVariable Long userId){
        return accountService.showAll(userId);
    }

    @GetMapping("/account/{accountId}")
    public List<HistoryResponse> showAllHistories(@PathVariable Long userId, @PathVariable Long accountId){
        return historyService.showAll(accountId);
    }

    @PostMapping("/account")
    public ResponseEntity<AccountResponse> createAccount(@PathVariable Long userId, @RequestBody AccountRequest accountRequest){
        Account createdAccount = accountService.createAccount(userId,accountRequest);

        AccountResponse accountResponse = AccountResponse.from(createdAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponse);
    }






}
