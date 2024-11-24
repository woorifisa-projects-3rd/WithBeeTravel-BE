package withbeetravel.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * RTR을 적용하기 위한 RefreshToken 저장 객체
 * - RTR (Refresh Token Rotation) : Refresh Token을 단 한 번만 사용할 수 있도록 하는 방법
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken {

    protected static final Map<String, Long> refreshTokens = new HashMap<>();

    public static Long getRefreshToken(final String refreshToken) {
        return Optional.ofNullable(refreshTokens.get(refreshToken))
                .orElseThrow(() -> new CustomException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }

    public static void putRefreshToken(final String refreshToken, Long id) {
        refreshTokens.put(refreshToken, id);
    }

    private static void removeRefreshToken(final String refreshToken) {
        refreshTokens.remove(refreshToken);
    }

    public static void removeUserRefreshToken(final long refreshToken) {
        for (Map.Entry<String, Long> entry : refreshTokens.entrySet()) {
            if (entry.getValue() == refreshToken) {
                removeRefreshToken(entry.getKey());
            }
        }

    }
}
