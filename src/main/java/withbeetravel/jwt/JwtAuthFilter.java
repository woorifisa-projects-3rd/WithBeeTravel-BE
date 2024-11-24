package withbeetravel.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import withbeetravel.dto.response.ErrorResponse;
import withbeetravel.exception.error.AuthErrorCode;
import withbeetravel.service.auth.CustomUserDetailsService;

import java.io.IOException;

// jwt의 검증 필터 수행
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        // jwt 헤더가 있을 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            // jwt 유효성 검증
            if (jwtUtil.isValidToken(token)) {
                Long userId = jwtUtil.getUserId(token);

                // 유저와 토큰 일치 시 userDetails 생성
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId.toString());

                if (userDetails != null) {
                    // UserDetails, Password, Role -> 접근 권한 인증 Token 생성
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    // 현재 request의 Security Context에 접근 권한 설정
                    SecurityContextHolder.getContext()
                            .setAuthentication(usernamePasswordAuthenticationToken);
                }
            } else {
                if (!jwtUtil.isValidToken(token)) {
                    jwtExceptionHandler(response, AuthErrorCode.EXPIRED_JWT);
                    return; // 필터 체인 중단
                } else {
                    jwtExceptionHandler(response, AuthErrorCode.INVALID_JWT);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response); // 다음 필터로 넘김
    }

    public void jwtExceptionHandler(HttpServletResponse response, AuthErrorCode authErrorCode) {
        try {
            // ErrorResponse 생성
            ResponseEntity<ErrorResponse> errorResponseEntity = ErrorResponse.toResponseEntity(authErrorCode);

            // Http 상태 코드 설정
            response.setStatus(errorResponseEntity.getStatusCodeValue());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // JSON으로 변환하여 응답 본문에 작성
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(errorResponseEntity.getBody());
            response.getWriter().write(jsonResponse);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
