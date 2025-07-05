package everTale.everTale_be.domain.profile.dto.response;

import everTale.everTale_be.domain.profile.domain.Profile;
import everTale.everTale_be.domain.profile.dto.ProfileSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Schema(description = "프로필 리스트 응답 DTO")
public class ProfileListResponseDto {

    @Schema(description = "프로필 목록")
    private List<ProfileSummary> profiles;

    public static ProfileListResponseDto from(List<Profile> profiles){
        List<ProfileSummary> summaryList = profiles.stream()
                .map(ProfileSummary::from)
                .collect(Collectors.toList());

        return ProfileListResponseDto.builder()
                .profiles(summaryList)
                .build();
    }
}
