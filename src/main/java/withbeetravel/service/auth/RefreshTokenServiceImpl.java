package withbeetravel.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import withbeetravel.dto.response.auth.SignInResponseDto;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.jwt.JwtUtil;
import withbeetravel.jwt.RefreshToken;
import withbeetravel.jwt.TokenStatus;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final JwtUtil jwtUtil;

    @Override
    public SignInResponseDto refreshToken(final String refreshToken) {
        // refresh token 유효성 검사
        checkRefreshToken(refreshToken);

        // refresh token id 조회
        Long id = RefreshToken.getRefreshToken(refreshToken);

        // 새로운 Access Token 생성
        String newAccessToken = jwtUtil.generateAccessToken(String.valueOf(id));

        // 기존에 가지고 있는 사용자의 refresh token 제거
        RefreshToken.removeUserRefreshToken(id);

        // 새로운 refresh token 생성 후 저장
        String newRefreshToken = jwtUtil.generateRefreshToken(String.valueOf(id));
        RefreshToken.putRefreshToken(newRefreshToken, id);

        return SignInResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private void checkRefreshToken(final String refreshToken) {
        if (!jwtUtil.isValidToken(refreshToken).equals(TokenStatus.VALID)) {
            throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

}
