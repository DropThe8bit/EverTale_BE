package everTale.everTale_be.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "기관 수정 DTO")
public class InstitutionRequestDto {

    @Schema(description = "기관명", example = "새싹 유치원")
    private String institution;
}
