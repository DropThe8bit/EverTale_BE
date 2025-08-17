package everTale.everTale_be.domain.easterEgg.dto.easterEggLetter;

import everTale.everTale_be.domain.story.dto.StoryCollectionResponseDto;
import everTale.everTale_be.domain.story.entity.Story;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@Schema(description = "이스터에그 편지 책장 응답 DTO")
public class EasterEggLetterStoriesResponseDto {

    @Schema(description = "이스터에그 편지 책장 스토리 리스트")
    private StoryCollectionResponseDto easterEggVoiceStories;

    public static EasterEggLetterStoriesResponseDto of(Page<Story> stories) {
        return EasterEggLetterStoriesResponseDto.builder()
                .easterEggVoiceStories(StoryCollectionResponseDto.from(stories))
                .build();
    }
}
