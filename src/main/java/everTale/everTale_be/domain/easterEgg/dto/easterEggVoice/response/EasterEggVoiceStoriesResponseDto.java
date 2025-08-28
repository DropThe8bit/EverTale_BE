package everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.response;

import everTale.everTale_be.domain.story.dto.response.StoryCollectionResponseDto;
import everTale.everTale_be.domain.story.entity.Story;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@Schema(description = "이스터에그 음성 책장 응답 DTO")
public class EasterEggVoiceStoriesResponseDto {

    @Schema(description = "이스터에그 음성 책장 스토리 리스트")
    private StoryCollectionResponseDto easterEggVoiceStories;

    public static EasterEggVoiceStoriesResponseDto of(Page<Story> stories) {
        return EasterEggVoiceStoriesResponseDto.builder()
                .easterEggVoiceStories(StoryCollectionResponseDto.from(stories))
                .build();
    }
}
