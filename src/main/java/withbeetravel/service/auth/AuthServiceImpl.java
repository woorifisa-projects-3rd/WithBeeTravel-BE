package withbeetravel.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.RefreshToken;
import withbeetravel.domain.User;
import withbeetravel.dto.request.auth.CustomUserInfoDto;
import withbeetravel.domain.RoleType;
import withbeetravel.dto.request.auth.SignInRequestDto;
import withbeetravel.dto.request.auth.SignUpRequestDto;
import withbeetravel.dto.response.auth.ExpirationDto;
import withbeetravel.dto.response.auth.SignInResponseDto;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.jwt.JwtUtil;
import withbeetravel.jwt.TokenStatus;
import withbeetravel.repository.RefreshTokenRepository;
import withbeetravel.repository.UserRepository;

import java.util.Date;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new CustomException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .name(signUpRequestDto.getName())
                .email(signUpRequestDto.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpRequestDto.getPassword()))
                .profileImage((int) (Math.random() * 10) + 1)
                .pinNumber(signUpRequestDto.getPinNumber())
                .failedPinCount(0)
                .pinLocked(false)
                .roleType(RoleType.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    public SignInResponseDto login(SignInRequestDto signInRequestDto) {
        String email = signInRequestDto.getEmail();
        String password = signInRequestDto.getPassword();
        Optional<User> user = userRepository.findUserByEmail(email);
        if (!userRepository.existsByEmail(email)) {
            throw new CustomException(AuthErrorCode.EMAIL_NOT_FOUND);
        }

        if (!bCryptPasswordEncoder.matches(password, user.get().getPassword())) {
            throw new CustomException(AuthErrorCode.INVALID_PASSWORD);
        }

        CustomUserInfoDto info = CustomUserInfoDto.from(user.get());

        // jwt 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(String.valueOf(info.getId()));

        // 기존에 가지고 있는 사용자의 refresh token 제거
        if (refreshTokenRepository.existsByUserId(info.getId())) {
            refreshTokenRepository.deleteByUserId(info.getId());
        }

        // refresh token 생성 후 저장
        String refreshToken = jwtUtil.generateRefreshToken(String.valueOf(info.getId()));
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .user(user.get())
                        .token(refreshToken)
                        .expirationTime(jwtUtil.getExpirationDateFromToken(refreshToken))
                        .build());

        return SignInResponseDto.of(accessToken, refreshToken);
    }

    @Override
    public SignInResponseDto refreshToken(final String refreshToken) {
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

        return SignInResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private RefreshToken validateRefreshTokenExists(String refreshToken) {
        return refreshTokenRepository
                .findByToken(refreshToken).orElseThrow(
                        (() -> new CustomException(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND)));
    }

    @Override
    public ExpirationDto checkExpirationTime(final String refreshToken) {
        Date expirationDateFromToken = jwtUtil.getExpirationDateFromToken(refreshToken);
        return ExpirationDto.from(expirationDateFromToken);
    }

    @Override
    public void logout(String refreshToken) {
        if (!isNull(refreshToken)) {
            refreshTokenRepository.deleteByToken(validateRefreshTokenExists(refreshToken).getToken());
        }
    }

    private void checkRefreshToken(final String refreshToken) {
        if (!jwtUtil.isValidToken(refreshToken).equals(TokenStatus.VALID)) {
            throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}