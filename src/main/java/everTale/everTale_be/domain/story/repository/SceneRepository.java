package everTale.everTale_be.domain.story.repository;

import everTale.everTale_be.domain.story.entity.Scene;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SceneRepository extends JpaRepository<Scene, Long> {
    @EntityGraph(attributePaths = {
            "story",
            "story.character",
            "story.character.characterPersonalities",
            "story.character.characterPersonalities.personality"
    })
    Optional<Scene> findByStoryIdAndPageAndStoryProfileId(Long storyId, int page, Long profileId);

    List<Scene> findAllByStoryIdAndStoryProfileId(Long storyId,Long profileId);

    List<Scene> findAllByStoryIdAndStoryProfileIdAndQuizIsNull(Long storyId, Long profileId);




}
