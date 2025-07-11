package everTale.everTale_be.domain.character.dto;

import everTale.everTale_be.domain.character.entity.StoryCharacter;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CharacterDetailResponseDto {

    private String name;
    private int age;
    private String gender;
    private List<String> personalities;
    private String storyTitle;

    public static CharacterDetailResponseDto from(StoryCharacter character){
        return CharacterDetailResponseDto.builder()
                .name(character.getName())
                .age(character.getAge())
                .gender(String.valueOf(character.getGender()))
                .personalities(character.getCharacterPersonalities().stream()
                            .map(characterPersonality -> characterPersonality.getPersonality().getPersonality())
                            .collect(Collectors.toList()))
                .storyTitle(character.getStory().getTitle())
                .build();
    }
}
