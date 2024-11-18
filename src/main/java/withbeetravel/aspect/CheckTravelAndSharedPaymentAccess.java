package withbeetravel.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Travel과 shared payment에 대한 권한 검사를 적용할 메소드에 사용
 */

@Target(ElementType.METHOD) // 메소드단에서 사용
@Retention(RetentionPolicy.RUNTIME) // 런타임 시점에 유지
public @interface CheckTravelAndSharedPaymentAccess {

    // 해당 애노테이션을 사용한 메소드에서 가져올 파라미터 이름
    String travelIdParam() default "travelId";
    String sharedPaymentIdParam() default "sharedPaymentId";
}