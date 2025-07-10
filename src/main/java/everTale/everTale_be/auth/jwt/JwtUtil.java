package everTale.everTale_be.auth.jwt;

import everTale.everTale_be.domain.user.domain.User;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.secret-key}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 1주일

    // JWT access token 생성
    public String generateAccessToken(User user) {
        Date generated = new Date(System.currentTimeMillis());
        Date accessTokenExpiredAt = new Date(generated.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .claim("userId", user.getId())
                .issuedAt(generated)
                .expiration(accessTokenExpiredAt)
                .signWith(secretKey)
                .compact();
    }

    // 프로필 선택 후, 프로필 ID가 추가된 JWT 토큰 생성
    public String generateAccessTokenWithProfile(Long userId, Long profileId) {
        Date generated = new Date(System.currentTimeMillis());
        Date accessTokenExpiredAt = new Date(generated.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .claim("userId", userId)  // userId
                .claim("profileId", profileId)  // profileId 추가
                .issuedAt(generated)
                .expiration(accessTokenExpiredAt)
                .signWith(secretKey)
                .compact();
    }

    // JWT refresh token 생성
    public String generateRefreshToken(User user) {
        Date generated = new Date(System.currentTimeMillis());
        Date refreshTokenExpiredAt = new Date(generated.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .claim("userId", user.getId())
                .issuedAt(generated)
                .expiration(refreshTokenExpiredAt)
                .signWith(secretKey)
                .compact();
    }

    public String extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnAuthorizedHandler(ErrorStatus._UNAUTHORIZED);
        }
        return header.substring(7);
    }

    // Claim 추출
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("JWT 유효성 실패: {} - {}", e.getClass().getSimpleName(), e.getMessage());
            return false;
        }
    }

    public long getAccessTokenExpireTime() {
        return ACCESS_TOKEN_EXPIRE_TIME;
    }

    public long getRefreshTokenExpireTime() {
        return REFRESH_TOKEN_EXPIRE_TIME;
    }
}
