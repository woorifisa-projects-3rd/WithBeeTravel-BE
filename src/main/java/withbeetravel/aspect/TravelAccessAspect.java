package withbeetravel.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.repository.TravelMemberRepository;
import withbeetravel.repository.TravelRepository;

/**
 * 메소드 실행 전 Travel에 대한 권한 검증
 */

@Aspect
@Component
@RequiredArgsConstructor
public class TravelAccessAspect {

    private final TravelRepository travelRepository;
    private final TravelMemberRepository travelMemberRepository;

    private final Long userId = 1L;

    @Before("@annotation(checkTravelAccess)") // @CheckTravelAccess가 붙은 메소드 실행 전에 실행
    public void checkAccess(JoinPoint joinPoint, CheckTravelAccess checkTravelAccess) {

        // @CheckTravelAccess를 붙인 메소드의 파라미터에서 travelId 추출
        Long travelId = getTravelIdFromArgs(joinPoint, checkTravelAccess.travelIdParam());

        // travelId에 해당하는 travel이 있는지 확인(없다면 예외 던지기)
        travelRepository.findById(travelId)
                .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_NOT_FOUND));

        // 권한 검사
        travelMemberRepository.findByTravelIdAndUserId(travelId, userId)
                .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_ACCESS_FORBIDDEN));
    }

    private Long getTravelIdFromArgs(JoinPoint joinPoint, String travelIdParam) {

        // 메소드 파라미터와 이름을 매핑하여 travelId를 찾아 반환
        String[] paramNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < paramNames.length; i++) {
            if(paramNames[i].equals(travelIdParam) && args[i] instanceof Long) {
                return (Long) args[i];
            }
        }

        // @CheckTravelAccess를 붙인 메서드에서 "travelId"라는 파라미터를 찾지 못한 경우
        throw new IllegalArgumentException("travelId 파라미터를 찾을 수 없습니다.");
    }
}