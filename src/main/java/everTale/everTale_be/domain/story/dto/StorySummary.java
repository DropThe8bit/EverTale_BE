package everTale.everTale_be.domain.story.dto;

import everTale.everTale_be.domain.story.entity.Story;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "스토리 요약 정보")
public class StorySummary {

    @Schema(description = "스토리 ID", example = "1")
    private Long storyId;

    @Schema(description = "대표 이미지 URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    @Schema(description = "스토리 제목", example = "루나의 모험")
    private String title;

    @Schema(description = "작가 이름", example = "김이화")
    private String authorName;

    public static StorySummary from(Story story){
        return StorySummary.builder()
                .storyId(story.getId())
                .imageUrl(story.getImageUrl())
                .title(story.getTitle())
                .authorName(story.getProfile().getName())
                .build();
    }
}
