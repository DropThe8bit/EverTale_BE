package everTale.everTale_be.domain.profile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Schema(description = "자녀 기관명", example = "새싹 유치원")
public class ChildProfileUpdateRequestDto {
    @Schema(description = "자녀 이름", example = "김이화")
    private String name;

    @Schema(description = "자녀 생년월일", example = "2001-07-16")
    private LocalDate birthDate;

    @Schema(description = "자녀 기관명", example = "새싹 유치원")
    private String institution;
}
