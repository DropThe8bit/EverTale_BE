package everTale.everTale_be.domain.voice.dto.response;

import everTale.everTale_be.domain.voice.entity.Voice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoiceSummary {

    @Schema(description = "음성 ID", example = "1")
    private Long voiceId;

    @Schema(description = "등록한 음성 이름", example = "엄마")
    private String name;

    public static VoiceSummary from(Voice voice){
        return VoiceSummary.builder()
                .voiceId(voice.getId())
                .name(voice.getName())
                .build();
    }
}
