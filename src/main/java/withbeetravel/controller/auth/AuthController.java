package withbeetravel.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import withbeetravel.dto.request.auth.SignInRequest;
import withbeetravel.dto.request.auth.SignUpRequest;
import withbeetravel.dto.response.SuccessResponse;
import withbeetravel.dto.response.auth.AccessTokenResponse;
import withbeetravel.dto.response.auth.ExpirationResponse;
import withbeetravel.dto.response.auth.SignInResponse;
import withbeetravel.service.auth.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/join")
    public SuccessResponse<Void> register(@RequestBody @Valid SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return SuccessResponse.of(HttpStatus.CREATED.value(), "회원 가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<AccessTokenResponse>> login(@RequestBody @Valid SignInRequest signInRequest) {
        SignInResponse signInResponse = authService.login(signInRequest);
        System.out.println("AccessTokenDto: " + signInResponse.getAccessTokenResponse());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, String.valueOf(cookieUtil.createHttpOnlyCookie(signInResponse.getRefreshToken())))
                        .body(SuccessResponse.of(HttpStatus.OK.value(), "로그인 성공", signInResponse.getAccessTokenResponse()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse<AccessTokenResponse>> reissue(@RequestHeader("refreshToken") String refreshToken) {
        SignInResponse signInResponse = authService.reissue(refreshToken);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        String.valueOf(cookieUtil.createHttpOnlyCookie(signInResponse.getRefreshToken())))
                .body(SuccessResponse.of(HttpStatus.OK.value(), "토큰 재발급 성공", signInResponse.getAccessTokenResponse()));
    }

    @GetMapping("/check-time")
    public SuccessResponse<ExpirationResponse> checkTokenTime(@RequestHeader("token") String token) {
        ExpirationResponse expirationResponse = authService.checkExpirationTime(token);
        return SuccessResponse.of(HttpStatus.OK.value(), "토큰 만료시간 조회 성공", expirationResponse);
    }

    @PostMapping("/logout")
    public SuccessResponse<Void> logout(@RequestHeader("refreshToken") String refreshToken) {
        authService.logout(refreshToken);
        return SuccessResponse.of(HttpStatus.OK.value(),  "로그아웃 성공");
    }

}

