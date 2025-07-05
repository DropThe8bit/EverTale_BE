package everTale.everTale_be.domain.story.dto;

import everTale.everTale_be.domain.story.entity.Scene;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SceneResponseDTO {
    private int sceneNum;
    private String content;
    private String imageUrl;

    public static SceneResponseDTO from(Scene scene) {
        return SceneResponseDTO.builder()
                .sceneNum(scene.getPage())
                .content(scene.getContent())
                .imageUrl(scene.getImageUrl())
                .build();
    }
}
