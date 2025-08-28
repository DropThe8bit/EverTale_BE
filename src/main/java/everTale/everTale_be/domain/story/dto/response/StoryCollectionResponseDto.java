package everTale.everTale_be.domain.story.dto.response;

import everTale.everTale_be.domain.story.dto.StorySummary;
import everTale.everTale_be.domain.story.entity.Story;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@Schema(description = "스토리 책장 응답 DTO")
public class StoryCollectionResponseDto {

    @Schema(description = "스토리 요약 정보 리스트")
    private List<StorySummary> storySummaries;

    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    private int currentPage;

    @Schema(description = "전체 페이지 수", example = "5")
    private int totalPage;

    @Schema(description = "전체 스토리 개수", example = "123")
    private long totalCount;

    public static StoryCollectionResponseDto from(Page<Story> stories){
        return StoryCollectionResponseDto.builder()
                .storySummaries(stories.stream().map(StorySummary::from).toList())
                .currentPage(stories.getNumber())
                .totalPage(stories.getTotalPages())
                .totalCount(stories.getTotalElements())
                .build();
    }
}
