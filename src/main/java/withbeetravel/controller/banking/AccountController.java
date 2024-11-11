package withbeetravel.controller.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import withbeetravel.domain.Account;
import withbeetravel.service.banking.AccountService;

import java.util.List;

@RestController
@RequestMapping("/banking/{userId}")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/account")
    public List<Account> list(@PathVariable Long userId){
        return accountService.showAll(userId);
    }


}
