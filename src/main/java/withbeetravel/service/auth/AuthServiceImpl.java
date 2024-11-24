package withbeetravel.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import withbeetravel.domain.User;
import withbeetravel.dto.request.auth.CustomUserInfoDto;
import withbeetravel.dto.request.auth.RoleType;
import withbeetravel.dto.request.auth.SignInRequestDto;
import withbeetravel.dto.request.auth.SignUpRequestDto;
import withbeetravel.dto.response.auth.SignInResponseDto;
import withbeetravel.exception.CustomException;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.jwt.JwtUtil;
import withbeetravel.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new CustomException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .name(signUpRequestDto.getName())
                .email(signUpRequestDto.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpRequestDto.getPassword()))
                .profileImage(1)
                .pinNumber(signUpRequestDto.getPinNumber())
                .failedPinCount(0)
                .accountLocked(false)
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
        String accessToken = jwtUtil.createAccessToken(info);
        return SignInResponseDto.from(accessToken);
    }
}