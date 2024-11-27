package withbeetravel.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import withbeetravel.domain.RefreshToken;
import withbeetravel.domain.User;
import withbeetravel.dto.request.auth.RefreshTokenDto;
import withbeetravel.dto.response.auth.ExpirationDto;
import withbeetravel.dto.response.auth.SignInResponseDto;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.jwt.JwtUtil;
import withbeetravel.jwt.TokenStatus;
import withbeetravel.repository.RefreshTokenRepository;
import withbeetravel.repository.UserRepository;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public SignInResponseDto refreshToken(final String refreshToken) {
        // refresh token 유효성 검사
        checkRefreshToken(refreshToken);

        // refresh token id 조회 (db에 존재하지 않으면 에러 처리)
        RefreshToken foundRefreshToken = refreshTokenRepository
                .findByToken(refreshToken).orElseThrow(
                        (() -> new CustomException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)));
        Long id = foundRefreshToken.getUser().getId();;

        // 새로운 Access Token 생성
        String newAccessToken = jwtUtil.generateAccessToken(String.valueOf(id));

        // 기존에 가지고 있는 사용자의 refresh token 제거
        refreshTokenRepository.deleteByUserId(id);

        // 새로운 refresh token 생성 후 저장
        String newRefreshToken = jwtUtil.generateRefreshToken(String.valueOf(id));
        Date expirationDateFromToken = jwtUtil.getExpirationDateFromToken(newRefreshToken);
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .user(user)
                        .token(newRefreshToken)
                        .expirationTime(expirationDateFromToken)
                        .build());

        return SignInResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public ExpirationDto checkExpirationTime(RefreshTokenDto refreshTokenDto) {
        Date expirationDateFromToken = jwtUtil.getExpirationDateFromToken(refreshTokenDto.getRefreshToken());
        return ExpirationDto.from(expirationDateFromToken);
    }

    private void checkRefreshToken(final String refreshToken) {
        if (!jwtUtil.isValidToken(refreshToken).equals(TokenStatus.VALID)) {
            throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

}
