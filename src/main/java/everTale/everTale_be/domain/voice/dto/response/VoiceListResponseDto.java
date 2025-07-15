package everTale.everTale_be.domain.voice.dto.response;

import everTale.everTale_be.domain.voice.domain.Voice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class VoiceListResponseDto {

    @Schema(description = "사용자가 등록한 음성 요약 리스트")
    private List<VoiceSummary> voiceSummaries;

    public static VoiceListResponseDto from(List<Voice> voices){
        return VoiceListResponseDto.builder()
                .voiceSummaries(voices.stream()
                            .map(VoiceSummary::from)
                            .collect(Collectors.toList()))
                .build();
    }
}
