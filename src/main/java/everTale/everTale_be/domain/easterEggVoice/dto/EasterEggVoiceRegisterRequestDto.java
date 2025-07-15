package everTale.everTale_be.domain.easterEggVoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EasterEggVoiceRegisterRequestDto {

    @JsonProperty("xCoordinate")
    private float xCoordinate;
    @JsonProperty("yCoordinate")
    private float yCoordinate;

    private float width;
    private float height;
}
