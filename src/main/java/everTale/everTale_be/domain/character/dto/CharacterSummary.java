package everTale.everTale_be.domain.character.dto;

import everTale.everTale_be.domain.character.entity.StoryCharacter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterSummary {

    private Long characterId;
    private String name;
    private String storyTitle;
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
