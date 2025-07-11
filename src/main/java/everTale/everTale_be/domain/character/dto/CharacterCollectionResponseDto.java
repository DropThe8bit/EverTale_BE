package everTale.everTale_be.domain.character.dto;

import lombok.Builder;
import lombok.Getter;
import everTale.everTale_be.domain.character.entity.StoryCharacter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class CharacterCollectionResponseDto {

    private List<CharacterSummary> characterSummaries;
    private int currentPage;
    private int totalPage;
    private long totalCount;

    public static CharacterCollectionResponseDto from(Page<StoryCharacter> characters){
        return CharacterCollectionResponseDto.builder()
                .characterSummaries(characters.stream().map(CharacterSummary::from).toList())
                .currentPage(characters.getNumber())
                .totalPage(characters.getTotalPages())
                .totalCount(characters.getTotalElements())
                .build();
    }
}
