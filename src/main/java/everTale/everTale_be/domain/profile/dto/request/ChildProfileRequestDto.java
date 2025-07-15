package everTale.everTale_be.domain.profile.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import everTale.everTale_be.domain.profile.entity.Enum.ProfileType;
import everTale.everTale_be.domain.profile.entity.Profile;
import everTale.everTale_be.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Schema(description = "자녀 프로필 생성 요청 DTO")
public class ChildProfileRequestDto {
    @NotBlank
    @Schema(description = "자녀 이름", example = "김이화")
    private String name;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "자녀 생년월일", example = "2001-07-16")
    private LocalDate birthDate;

    @NotBlank
    @Schema(description = "자녀 기관명", example = "새싹 유치원")
    private String institution;

    public Profile toEntity(User user, ProfileType profileType){
        return Profile.builder()
                .name(name)
                .birthDate(birthDate)
                .institution(institution)
                .profileType(profileType)
                .user(user)
                .build();
    }
}
