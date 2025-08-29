package everTale.everTale_be.domain.story.dto.response;

import everTale.everTale_be.domain.story.entity.Scene;
import everTale.everTale_be.domain.story.entity.Story;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class StoryResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StoryScenesResponseDTO {
        private Long storyId;
        private Long profileId;
        private String title;
        private List<SceneResponseDTO> scenes;

        public static StoryScenesResponseDTO of(Story story, List<Scene> sceneList) {
            return StoryScenesResponseDTO.builder()
                    .storyId(story.getId())
                    .profileId(story.getProfile().getId())
                    .title(story.getTitle())
                    .scenes(sceneList.stream()
                            .map(SceneResponseDTO::from)
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
