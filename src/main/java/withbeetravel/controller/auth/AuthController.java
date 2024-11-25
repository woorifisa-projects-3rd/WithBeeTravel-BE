package withbeetravel.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import withbeetravel.dto.request.auth.RefreshTokenDto;
import withbeetravel.dto.request.auth.SignInRequestDto;
import withbeetravel.dto.request.auth.SignUpRequestDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.auth.SignInResponseDto;
import withbeetravel.service.auth.AuthService;
import withbeetravel.service.auth.RefreshTokenService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/join")
    public SuccessResponse<Void> register(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        return SuccessResponse.of(HttpStatus.CREATED.value(), "회원 가입 완료");
    }

    @PostMapping("/login")
    public SuccessResponse<SignInResponseDto> signIn(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        SignInResponseDto signInResponseDto = authService.login(signInRequestDto);
        return SuccessResponse.of(HttpStatus.OK.value(), "로그인 성공", signInResponseDto);
    }

    @PostMapping("/token-refresh")
    public SuccessResponse<SignInResponseDto> refreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto) {
        SignInResponseDto signInResponseDto = refreshTokenService.refreshToken(refreshTokenDto.getRefreshToken());
        return SuccessResponse.of(HttpStatus.OK.value(), "토큰 재발급 성공", signInResponseDto);
    }
}

