package withbeetravel.security.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import withbeetravel.domain.Travel;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.TravelErrorCode;
import withbeetravel.repository.TravelMemberRepository;
import withbeetravel.repository.TravelRepository;
import withbeetravel.security.annotation.CheckTravelAccess;

import java.util.Optional;

/**
 * 메소드 실행 전 Travel에 대한 권한 검증
 */

@Aspect
@Component
@RequiredArgsConstructor
public class TravelAccessAspect {

    private final TravelRepository travelRepository;
    private final TravelMemberRepository travelMemberRepository;

    @Before("@annotation(checkTravelAccess)") // @CheckTravelAccess이 붙은 메소드에 적용
    public void checkAccess(JoinPoint joinPoint, CheckTravelAccess checkTravelAccess) {

        // 로그인된 회원 id
        Long userId = 3L;

        // @CheckTravelAccess를 붙인 메소드의 파라미터에서 travelId 추출
        Long travelId = getTravelIdFromArgs(joinPoint, checkTravelAccess.travelIdParam());

        // travelId에 해당하는 travel이 있는지 확인(없다면 예외 던지기)
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_NOT_FOUND));

        // 권한 검사
        travelMemberRepository.findByTravel_IdAndUser_Id(travelId, userId)
                .orElseThrow(() -> new CustomException(TravelErrorCode.TRAVEL_ACCESS_FORBIDDEN));
    }

    private Long getTravelIdFromArgs(JoinPoint joinPoint, String travelIdParam) {

        // 메소드 파라미터와 이름을 매핑하여 travelId를 찾아 반환
        var paramNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        var args = joinPoint.getArgs();

        for (int i = 0; i < paramNames.length; i++) {
            if(paramNames[i].equals(travelIdParam) && args[i] instanceof Long) {
                return (Long) args[i];
            }
        }

        // @CheckTravelAccess를 붙인 메서드에서 "travelId"라는 파라미터를 찾지 못한 경우
        throw new IllegalArgumentException("travelId 파라미터를 찾을 수 없습니다.");
    }
}
