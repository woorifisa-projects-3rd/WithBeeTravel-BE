package withbeetravel.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import withbeetravel.dto.request.auth.SignInRequestDto;
import withbeetravel.dto.request.auth.SignUpRequestDto;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.auth.ExpirationDto;
import withbeetravel.dto.response.auth.SignInResponseDto;
import withbeetravel.service.auth.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
    public SuccessResponse<SignInResponseDto> refreshToken(@RequestHeader("refreshToken") String refreshToken) {
        SignInResponseDto signInResponseDto = authService.refreshToken(refreshToken);
        return SuccessResponse.of(HttpStatus.OK.value(), "토큰 재발급 성공", signInResponseDto);
    }

    @GetMapping("/check-refresh")
    public SuccessResponse<ExpirationDto> checkTokenTime(@RequestHeader("refreshToken") String refreshToken) {
        ExpirationDto expirationDto = authService.checkExpirationTime(refreshToken);
        return SuccessResponse.of(HttpStatus.OK.value(), "리프레시 토큰 만료시간 조회 성공", expirationDto);
    }

    @PostMapping("/logout")
    public SuccessResponse<Void> logout(@RequestHeader("refreshToken") String refreshToken) {
        authService.logout(refreshToken);
        return SuccessResponse.of(HttpStatus.OK.value(),  "로그아웃 성공");
    }

}

