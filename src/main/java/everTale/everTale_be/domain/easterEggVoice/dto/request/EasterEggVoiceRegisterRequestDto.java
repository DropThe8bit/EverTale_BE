package everTale.everTale_be.domain.easterEggVoice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EasterEggVoiceRegisterRequestDto {

    @JsonProperty("xCoordinate")
    @Schema(description = "X 좌표", example = "120.5")
    private float xCoordinate;

    @JsonProperty("yCoordinate")
    @Schema(description = "Y 좌표", example = "200.0")
    private float yCoordinate;

    @Schema(description = "좌표 허용 너비", example = "30.0")
    private float width;

    @Schema(description = "좌표 허용 높이", example = "40.0")
    private float height;
}
