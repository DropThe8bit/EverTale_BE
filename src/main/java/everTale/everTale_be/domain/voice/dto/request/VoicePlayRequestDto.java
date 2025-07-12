package everTale.everTale_be.domain.voice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoicePlayRequestDto {

    @Schema(description = "음성으로 변환할 텍스트", example = "안녕, 나는 글로든이야 !")
    private String text;
}
