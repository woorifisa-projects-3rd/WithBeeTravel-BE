package withbeetravel.controller.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import withbeetravel.domain.Account;
import withbeetravel.dto.banking.account.AccountResponse;
import withbeetravel.service.banking.AccountService;

import java.util.List;

@RestController
@RequestMapping("/banking/{userId}")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/account")
    public List<AccountResponse> list(@PathVariable Long userId){
        return accountService.showAll(userId);
    }


}
