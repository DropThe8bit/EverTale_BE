package everTale.everTale_be.domain.character.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import everTale.everTale_be.domain.character.entity.StoryCharacter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@Schema(description = "캐릭터 책장 응답 DTO")
public class CharacterCollectionResponseDto {

    @Schema(description = "캐릭터 요약 리스트")
    private List<CharacterSummary> characterSummaries;

    @Schema(description = "현재 페이지 번호", example = "0")
    private int currentPage;

    @Schema(description = "전체 페이지 수", example = "5")
    private int totalPage;

    @Schema(description = "전체 캐릭터 수", example = "12")
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
