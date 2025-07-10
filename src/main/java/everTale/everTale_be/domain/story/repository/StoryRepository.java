package everTale.everTale_be.domain.story.repository;

import everTale.everTale_be.domain.story.entity.Story;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoryRepository extends JpaRepository<Story, Long> {
    @EntityGraph(attributePaths = {
            "character",
            "character.characterPersonalities",
            "character.characterPersonalities.personality"
    })
    Optional<Story> findByIdAndProfileId(Long storyId, Long profileId);

}
