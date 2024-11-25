package withbeetravel.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// SecurityContextHolder에서 로그인한 사용자 정보를 불러오는 Util
public class UserAuthorizationUtil {
    public UserAuthorizationUtil() {
        throw new AssertionError();
    }

    public static Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser().getId();
    }
}
