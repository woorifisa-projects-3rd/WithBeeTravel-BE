package withbeetravel.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import withbeetravel.dto.request.auth.CustomUserInfoDto;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

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

    // Access Token 생성
    public String createAccessToken(CustomUserInfoDto user) {
        return createToken(user, accessTokenExpTime);
    }

    // jwt 생성
    private String createToken(CustomUserInfoDto user, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("name", user.getName());
        claims.put("role", user.getRole()); // USER,ADMIN

        // 현재 시간 기준으로 토큰 발행 시간과 만료 시간 설정
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Token에서 User Id 추출
    public Long getUserId(String token) {
        return parseClaims(token).get("memberId", Long.class);
    }

    // jwt 검증
    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
        }
        return false;
    }

    // jwt claims 추출
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}