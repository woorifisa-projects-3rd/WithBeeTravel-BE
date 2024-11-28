package withbeetravel.controller.banking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.aspect.CheckBankingAccess;
import withbeetravel.dto.request.account.*;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.account.AccountConnectedWibeeResponse;
import withbeetravel.dto.response.account.AccountOwnerNameResponse;
import withbeetravel.dto.response.account.AccountResponse;
import withbeetravel.dto.response.account.HistoryResponse;
import withbeetravel.security.UserAuthorizationUtil;
import withbeetravel.service.banking.AccountService;
import withbeetravel.service.banking.HistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final HistoryService historyService;



    @GetMapping()
    public SuccessResponse<List<AccountResponse>> showAllAccount(){
        Long userId = UserAuthorizationUtil.getLoginUserId();
        return SuccessResponse.of(
                HttpStatus.OK.value(),
                "전체 계좌 조회 완료",
                accountService.showAll(userId)
        );
    }

    @GetMapping("/{accountId}/info")
    @CheckBankingAccess(accountIdParam = "accountId") // AOP로 권한 검증
    public SuccessResponse<AccountResponse> accountInfo(@PathVariable Long accountId){
        return  SuccessResponse.of(
                HttpStatus.OK.value(),
                "accountId로 계좌 조회 성공",
                accountService.accountInfo(accountId)
        );
    }

    @GetMapping("/{accountId}")
    @CheckBankingAccess(accountIdParam = "accountId") // AOP로 권한 검증
    public SuccessResponse<List<HistoryResponse>> showAllHistories(@PathVariable Long accountId){
        return SuccessResponse.of(
                HttpStatus.OK.value(),
                "계좌 거래내역 조회 성공",
                historyService.showAll(accountId)
        );
    }

    @PostMapping()
    public SuccessResponse<AccountResponse> createAccount(@RequestBody CreateAccountRequest createAccountRequest){

        Long userId = UserAuthorizationUtil.getLoginUserId();
        // 계좌 생성
        return SuccessResponse.of(
                HttpStatus.CREATED.value(),
                "계좌 생성 완료",
                accountService.createAccount(userId,createAccountRequest)
        );//1L 부분은 나중에 로그인 상태가 되면 유저 별로 바뀔 예정
    }

    @PostMapping("{accountId}/transfer")
    public SuccessResponse transfer(@RequestBody TransferRequest transferRequest){

        accountService.transfer(transferRequest.getAccountId(),
                transferRequest.getAccountNumber(),
                transferRequest.getAmount(),
                transferRequest.getRqspeNm());

        return SuccessResponse.of(
                HttpStatus.ACCEPTED.value(),
                "송금이 완료되었습니다."
        );
    }

    @PostMapping("{accountId}/deposit")
    public SuccessResponse deposit(
            @RequestBody DepositRequest depositRequest,
            @PathVariable Long accountId) {

        // 입금 처리
        accountService.deposit(accountId, depositRequest.getAmount(), depositRequest.getRqspeNm());

        // SuccessResponse 객체를 생성하여 반환
        return SuccessResponse.of(
                HttpStatus.ACCEPTED.value(), // 상태 코드 202
                "입금이 완료되었습니다." // 메시지
        );
    }

    @PostMapping("/verify")
    public SuccessResponse verifyAccount(@RequestBody AccountNumberRequest accountNumberRequest){

        accountService.verifyAccount(accountNumberRequest.getAccountNumber());

        return SuccessResponse.of(
                HttpStatus.OK.value(),
                "계좌 번호 존재 확인 완료"
        );

    }

    @PostMapping("/find-user")
    public SuccessResponse<AccountOwnerNameResponse> findUserNameByAccountNumber(@RequestBody AccountNumberRequest accountNumberRequest) {
        return  SuccessResponse.of(
                HttpStatus.OK.value(),
                "찾은 계좌 주인 이름",
                accountService.findUserNameByAccountNumber(accountNumberRequest.getAccountNumber())
        );
    }

    @GetMapping("/{accountId}/check-wibee")
    public SuccessResponse<AccountConnectedWibeeResponse> connectedWibee(
            @PathVariable Long accountId){
        return SuccessResponse.of(
                HttpStatus.OK.value(),
                "위비 카드 연결 여부 확인 완료",
                accountService.connectedWibee(accountId)
        );
    }

}
