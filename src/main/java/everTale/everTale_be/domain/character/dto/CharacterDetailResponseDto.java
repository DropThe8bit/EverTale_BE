package everTale.everTale_be.domain.character.dto;

import everTale.everTale_be.domain.character.entity.StoryCharacter;
import everTale.everTale_be.domain.profile.entity.Profile;
import everTale.everTale_be.domain.story.entity.Story;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Schema(description = "캐릭터 상세 조회 응답 DTO")
public class CharacterDetailResponseDto {

    @Schema(description = "캐릭터 이미지 URL", example = "https://example.com/image.png")
    private String imageUrl;

    @Schema(description = "캐릭터 이름", example = "글로든")
    private String name;

    @Schema(description = "캐릭터 나이", example = "11")
    private int age;

    @Schema(description = "캐릭터 성별", example = "MALE")
    private String gender;

    @Schema(description = "캐릭터 성격 목록", example = "[\"활발함\", \"외향적\"]")
    private List<String> personalities;

    @Schema(description = "캐릭터가 등장하는 스토리 ID", example = "5")
    private Long storyId;

    @Schema(description = "캐릭터가 등장하는 스토리 제목", example = "엘리오")
    private String storyTitle;

    @Schema(description = "작가 이름", example = "김이화")
    private String authorName;

    public static CharacterDetailResponseDto from(StoryCharacter character, Story story, Profile author){
        return CharacterDetailResponseDto.builder()
                .imageUrl(character.getImageUrl())
                .name(character.getName())
                .age(character.getAge())
                .gender(String.valueOf(character.getGender()))
                .personalities(character.getCharacterPersonalities().stream()
                            .map(characterPersonality -> characterPersonality.getPersonality().getPersonality())
                            .collect(Collectors.toList()))
                .storyId(story.getId())
                .storyTitle(story.getTitle())
                .authorName(author.getName())
                .build();
    }
}
