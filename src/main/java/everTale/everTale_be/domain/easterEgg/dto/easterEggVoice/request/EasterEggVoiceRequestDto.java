package everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EasterEggVoiceRequestDto {

    @JsonProperty("xCoordinate")
    @Schema(description = "사용자가 클릭한 이미지의 X 좌표", example = "135.0")
    private float xCoordinate;

    @JsonProperty("yCoordinate")
    @Schema(description = "사용자가 클릭한 이미지의 Y 좌표", example = "210.0")
    private float yCoordinate;
}
