package everTale.everTale_be.domain.easterEggVoice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EasterEggVoiceRequestDto {

    @Schema(description = "사용자가 클릭한 이미지의 X 좌표", example = "135.0")
    private float xCoordinate;

    @Schema(description = "사용자가 클릭한 이미지의 Y 좌표", example = "210.0")
    private float yCoordinate;
}
