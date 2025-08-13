package everTale.everTale_be.domain.profile.dto.response;

import everTale.everTale_be.auth.jwt.JwtUtil;
import everTale.everTale_be.domain.profile.entity.Enum.ProfileType;
import everTale.everTale_be.domain.profile.entity.Profile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "프로필 접속 응답 DTO")
public class ProfileEnterResponseDto {

    @Schema(description = "프로필 ID", example = "1")
    private Long profileId;

    @Schema(description = "프로필 타입", example = "PARENT")
    private ProfileType profileType;

    @Schema(description = "프로필 Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(description = "프로필 Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "Access Token 만료 시간(ms)", example = "1755015103057")
    private long accessExp;

    @Schema(description = "Refresh Token 만료 시간(ms)", example = "1755015103057")
    private long refreshExp;

    public static ProfileEnterResponseDto from(Profile profile, String accessToken, String refreshToken, JwtUtil jwtUtil){
        return ProfileEnterResponseDto.builder()
                .profileId(profile.getId())
                .profileType(profile.getProfileType())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessExp(jwtUtil.getAccessTokenExpireTime())
                .refreshExp(jwtUtil.getRefreshTokenExpireTime())
                .build();
    }
}
