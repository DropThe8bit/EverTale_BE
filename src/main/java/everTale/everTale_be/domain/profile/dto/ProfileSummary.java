package everTale.everTale_be.domain.profile.dto;

import everTale.everTale_be.domain.profile.entity.Profile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@Schema(description = "프로필 요약")
public class ProfileSummary {
    @Schema(description = "프로필 ID", example = "1")
    private final Long profileId;

    @Schema(description = "이름", example = "김이화")
    private final String name;

    public static ProfileSummary from(Profile profile){
        return ProfileSummary.builder()
                .profileId(profile.getId())
                .name(profile.getName())
                .build();
    }
}
