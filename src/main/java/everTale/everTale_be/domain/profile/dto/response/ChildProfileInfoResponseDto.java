package everTale.everTale_be.domain.profile.dto.response;

import everTale.everTale_be.domain.profile.domain.Profile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@Schema(description = "자녀 프로필 회원 정보 응답 DTO")
public class ChildProfileInfoResponseDto implements ProfileInfoResponseDto{
    @Schema(description = "이름", example = "김이화")
    private String name;

    @Schema(description = "생년월일", example = "2001-07-16")
    private LocalDate birthDate;

    @Schema(description = "기관명", example = "새싹 유치원")
    private String institution;

    public static ChildProfileInfoResponseDto from(Profile profile){
        return ChildProfileInfoResponseDto.builder()
                .name(profile.getName())
                .birthDate(profile.getBirthDate())
                .institution(profile.getInstitution())
                .build();
    }
}
