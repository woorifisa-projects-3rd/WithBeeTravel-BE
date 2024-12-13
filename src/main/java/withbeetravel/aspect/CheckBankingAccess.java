package withbeetravel.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 메소드단에서 사용
@Retention(RetentionPolicy.RUNTIME) // 런타임 시점에 유지
public @interface CheckBankingAccess {

    String accountIdParam() default "accountId";
}
