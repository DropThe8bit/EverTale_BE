package everTale.everTale_be.domain.character.dto;

import everTale.everTale_be.domain.character.entity.StoryCharacter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "캐릭터 요약 정보")
public class CharacterSummary {

    @Schema(description = "캐릭터 ID", example = "1")
    private Long characterId;

    @Schema(description = "캐릭터 이름", example = "글로든")
    private String name;

    @Schema(description = "등장하는 스토리 제목", example = "엘리오")
    private String storyTitle;

    @Schema(description = "캐릭터 이미지 URL", example = "https://example.com/image.png")
    private String imageUrl;

    public static CharacterSummary from(StoryCharacter character){
        return CharacterSummary.builder()
                .characterId(character.getId())
                .name(character.getName())
                .storyTitle(character.getStory().getTitle())
                .imageUrl(character.getImageUrl())
                .build();
    }
}
