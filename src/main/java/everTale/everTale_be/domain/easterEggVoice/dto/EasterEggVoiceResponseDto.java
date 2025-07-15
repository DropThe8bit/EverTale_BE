package everTale.everTale_be.domain.easterEggVoice.dto;

import everTale.everTale_be.domain.easterEggVoice.entity.EasterEggVoice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EasterEggVoiceResponseDto {
    private float xCoordinate;
    private float yCoordinate;
    private float width;
    private float height;
    private String voiceUrl;

    public static EasterEggVoiceResponseDto from(EasterEggVoice voice){
        return EasterEggVoiceResponseDto.builder()
                .xCoordinate(voice.getXCoordinate())
                .yCoordinate(voice.getYCoordinate())
                .width(voice.getWidth())
                .height(voice.getHeight())
                .voiceUrl(voice.getVoiceFile())
                .build();
    }
}
