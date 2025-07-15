package everTale.everTale_be.auth.dto.response;

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
    @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1...")
    private String refreshToken;
    @Schema(description = "Access Token 만료 시간 (timestamp, ms)", example = "1717654800000")
    private long accessExp;
    @Schema(description = "Refresh Token 만료 시간 (timestamp, ms)", example = "1718259600000")
    private long refreshExp;
    @Schema(description = "유저 ID", example = "1")
    private Long userId;

    public static LoginTokenResponseDto of(User user, String accessToken, String refreshToken, JwtUtil jwtUtil){
        return LoginTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessExp(System.currentTimeMillis() + jwtUtil.getAccessTokenExpireTime())
                .refreshExp(System.currentTimeMillis() + jwtUtil.getRefreshTokenExpireTime())
                .userId(user.getId())
                .build();
    }
}
