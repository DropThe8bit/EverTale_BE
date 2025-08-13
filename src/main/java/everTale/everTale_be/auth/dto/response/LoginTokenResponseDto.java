package everTale.everTale_be.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import everTale.everTale_be.auth.jwt.JwtUtil;
import everTale.everTale_be.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "로그인 토큰 응답 DTO")
public class LoginTokenResponseDto {

    @Schema(description = "Access Token", example = "eyJhbGciOiJIUzI1...")
    private String accessToken;

    @Schema(description = "Access Token 만료 시간 (timestamp, ms)", example = "1717654800000")
    private long accessExp;

    @Schema(description = "유저 ID", example = "1")
    private Long userId;

    @Schema(description = "첫 가입 여부", example = "true")
    private boolean firstLogin;

    public static LoginTokenResponseDto of(User user, String accessToken, JwtUtil jwtUtil, boolean isFirstLogin) {
        return LoginTokenResponseDto.builder()
                .accessToken(accessToken)
                .accessExp(System.currentTimeMillis() + jwtUtil.getAccessTokenExpireTime())
                .userId(user.getId())
                .firstLogin(isFirstLogin)
                .build();
    }
}
