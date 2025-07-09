package everTale.everTale_be.domain.profile.dto.response;

import everTale.everTale_be.domain.profile.domain.Enum.ProfileType;
import everTale.everTale_be.domain.profile.domain.Profile;
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

    private String accessToken;

    public static ProfileEnterResponseDto from(Profile profile, String accessToken){
        return ProfileEnterResponseDto.builder()
                .profileId(profile.getId())
                .profileType(profile.getProfileType())
                .accessToken(accessToken)
                .build();
    }
}
