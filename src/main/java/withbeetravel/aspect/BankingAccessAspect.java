package withbeetravel.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import withbeetravel.domain.Account;
import withbeetravel.domain.User;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.BankingErrorCode;
import withbeetravel.repository.AccountRepository;
import withbeetravel.security.UserAuthorizationUtil;

@Aspect
@Component
@RequiredArgsConstructor
public class BankingAccessAspect {
    private final AccountRepository accountRepository;

    // 현재 로그인 된 userId

    @Before("@annotation(checkBankingAccess)") // @CheckBankingAccess 애노테이션이 붙은 메소드 실행 전에 실행
    public void checkAccess(JoinPoint joinPoint, CheckBankingAccess checkBankingAccess) {

        Long userId = UserAuthorizationUtil.getLoginUserId();

        // @CheckBankingAccess를 붙인 메소드의 파라미터에서 accountId 추출
        Long accountId = getAccountIdFromArgs(joinPoint, checkBankingAccess.accountIdParam());

        // accountId에 해당하는 계좌가 존재하는지 확인
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomException(BankingErrorCode.ACCOUNT_NOT_FOUND));

        // 권한 검사: 로그인된 사용자와 계좌 소유자가 일치하는지 확인
        Long accountUserId = account.getUser().getId();  // Account 객체에 User 정보가 포함되어 있다고 가정
        if (!userId.equals(accountUserId)) {
            throw new CustomException(BankingErrorCode.HISTORY_ACCESS_FORBIDDEN);
        }
    }

    private Long getAccountIdFromArgs(JoinPoint joinPoint, String accountIdParam) {
        // 메소드 파라미터와 이름을 매핑하여 accountId를 찾아 반환
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(accountIdParam) && args[i] instanceof Long) {
                return (Long) args[i];
            }
        }

        // @CheckBankingAccess를 붙인 메서드에서 "accountId"라는 파라미터를 찾지 못한 경우
        throw new IllegalArgumentException("accountId 파라미터를 찾을 수 없습니다.");
    }
}
