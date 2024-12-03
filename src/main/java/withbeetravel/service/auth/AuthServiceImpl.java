package withbeetravel.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.RefreshToken;
import withbeetravel.domain.User;
import withbeetravel.dto.request.auth.CustomUserInfo;
import withbeetravel.domain.RoleType;
import withbeetravel.dto.request.auth.SignInRequest;
import withbeetravel.dto.request.auth.SignUpRequest;
import withbeetravel.dto.response.auth.*;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.jwt.JwtUtil;
import withbeetravel.jwt.TokenStatus;
import withbeetravel.repository.RefreshTokenRepository;
import withbeetravel.repository.UserRepository;
import withbeetravel.service.log.LogService;

import java.util.Date;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LogService logService;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new CustomException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                .profileImage((int) (Math.random() * 10) + 1)
                .pinNumber(signUpRequest.getPinNumber())
                .failedPinCount(0)
                .pinLocked(false)
                .roleType(RoleType.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    public SignInResponse login(SignInRequest signInRequest) {
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new CustomException(AuthErrorCode.EMAIL_NOT_FOUND));

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            logService.logLoginFailed(user,email); // 로그인 실패 로그 저장
            throw new CustomException(AuthErrorCode.INVALID_PASSWORD);
        }

        CustomUserInfo info = CustomUserInfo.from(user);

        // jwt 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(String.valueOf(info.getId()));

        // 기존에 가지고 있는 사용자의 refresh token 제거
        refreshTokenRepository.deleteByUserId(info.getId());

        // refresh token 생성 후 저장
        String refreshToken = jwtUtil.generateRefreshToken(String.valueOf(info.getId()));
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .user(user)
                        .token(refreshToken)
                        .expirationTime(jwtUtil.getExpirationDateFromToken(refreshToken))
                        .build());

        // AccessTokenDto 생성
        UserAuthResponse userAuthResponse = UserAuthResponse.of(accessToken, user.getRoleType());

        // 로그인 성공 로그 기록
        logService.logLoginSuccess(user, email);

        return SignInResponse.of(userAuthResponse, refreshToken);
    }

    @Override
    public ReissueResponse reissue(final String refreshToken) {
        // refresh token 유효성 검사
        checkRefreshToken(refreshToken);

        // refresh token id 조회 (db에 존재하지 않으면 에러 처리)
        RefreshToken foundRefreshToken = validateRefreshTokenExists(refreshToken);
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

        // AccessTokenDto 생성
        AccessTokenResponse accessTokenResponse = AccessTokenResponse.builder().accessToken(newAccessToken).build();

        return ReissueResponse.of(accessTokenResponse, newRefreshToken);
    }

    private RefreshToken validateRefreshTokenExists(String refreshToken) {
        return refreshTokenRepository
                .findByToken(refreshToken).orElseThrow(
                        (() -> new CustomException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)));
    }

    @Override
    public ExpirationResponse checkExpirationTime(final String token) {
        Date expirationDateFromToken = jwtUtil.getExpirationDateFromToken(token);
        return ExpirationResponse.from(expirationDateFromToken);
    }

    @Override
    public void logout(String refreshToken) {
        if (!isNull(refreshToken)) {
            refreshTokenRepository.deleteByToken(validateRefreshTokenExists(refreshToken).getToken());
        }
    }

    @Override
    public MyPageResponse getMyPageInfo(Long userId) {

        User user = getUser(userId);

        return MyPageResponse.from(user);
    }

    private void checkRefreshToken(final String refreshToken) {
        if (!jwtUtil.isValidToken(refreshToken).equals(TokenStatus.VALID)) {
            throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));
    }
}