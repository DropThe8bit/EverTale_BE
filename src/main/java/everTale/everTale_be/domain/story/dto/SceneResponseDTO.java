package everTale.everTale_be.domain.story.dto;

import everTale.everTale_be.domain.story.entity.Scene;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SceneResponseDTO {
    private int pageNum;
    private Long sceneId;
    private String content;
    private String imageUrl;

    public static SceneResponseDTO from(Scene scene) {
        return SceneResponseDTO.builder()
                .pageNum(scene.getPage())
                .sceneId(scene.getId())
                .content(scene.getContent())
                .imageUrl(scene.getImageUrl())
                .build();
    }
}
