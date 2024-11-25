package withbeetravel.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// JwtUtil : jwt 생성 및 검증
@Slf4j
@Component
public class JwtUtil {

    // jwt 서명에 사용할 key
    private final Key key;

    // 액세스 토큰의 만료 시간
    private final long accessTokenExpTime;

    public JwtUtil(@Value("${jwt.secret}") final String secretKey,
                   @Value("${jwt.expiration_time}") final long accessTokenExpTime) {

        // secretKey를 Base64로 디코딩하여 SHA 알고리즘을 사용하도록 변환
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    // Token에서 UserName 조회
    public String getUserNameFromToken(final String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    // token의 사용자 속성 정보 조회
    public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {

        TokenStatus tokenStatus = isValidToken(token);

        if (tokenStatus.equals(TokenStatus.VALID)) {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } else { // 토큰 관련 에러가 발생했을 경우
            return (T) String.valueOf(tokenStatus);
        }
    }

    // token 사용자 모든 속성 정보 조회
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // Access Token 생성
    public String generateAccessToken(final String id) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setId(String.valueOf(id))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpTime)) // 30분
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 생성
    public String generateRefreshToken(final String id) {

        return Jwts.builder()
                .setId(String.valueOf(id))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (accessTokenExpTime * 2) * 24)) // 24시간
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    // jwt 검증
    public TokenStatus isValidToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return TokenStatus.VALID;
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT: {}", e.getMessage());
            return TokenStatus.EXPIRED;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            return TokenStatus.INVALID;
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT: {}", e.getMessage());
            return TokenStatus.UNSUPPORTED;
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage());
            return TokenStatus.EMPTY;
        }
    }
}