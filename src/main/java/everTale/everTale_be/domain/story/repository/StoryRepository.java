package everTale.everTale_be.domain.story.repository;

import everTale.everTale_be.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
}
