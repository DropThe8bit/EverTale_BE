package everTale.everTale_be.auth.jwt;

import everTale.everTale_be.domain.profile.domain.CustomProfileDetails;
import everTale.everTale_be.domain.user.domain.User;
import everTale.everTale_be.domain.user.repository.UserRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer "))
                ? bearerToken.substring(7) // Bearer 제거하고 토큰만 반환
                : null;
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    // 토큰에서 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = jwtUtil.extractClaims(token);
        Long userId = claims.get("userId", Long.class);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    }

    // profileId까지 들어 있는 토큰일 경우 사용
    public Authentication getAuthenticationWithProfile(String token) {
        Claims claims = jwtUtil.extractClaims(token);
        Long userId = claims.get("userId", Long.class);
        Long profileId = claims.get("profileId", Long.class);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));

        CustomProfileDetails customProfileDetails = new CustomProfileDetails(userId, profileId);

        return new UsernamePasswordAuthenticationToken(customProfileDetails, null, customProfileDetails.getAuthorities());
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = jwtUtil.extractClaims(token);
        return claims.get("userId", Long.class);
    }

    public boolean isTokenContainsProfileId(String token) {
        Claims claims = jwtUtil.extractClaims(token);
        return claims.containsKey("profileId");
    }
}
