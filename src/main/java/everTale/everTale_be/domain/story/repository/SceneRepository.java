package everTale.everTale_be.domain.story.repository;

import everTale.everTale_be.domain.story.entity.Scene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SceneRepository extends JpaRepository<Scene, Long> {
    @Query("SELECT s FROM Scene s WHERE s.story.id = :storyId AND s.page = :page")
    Optional<Scene> findByStoryIdAndPage(@Param("storyId") Long storyId, @Param("page") int page);

    @Query("SELECT s FROM Scene s WHERE s.story.id = :storyId")
    List<Scene> findAllByStoryId(@Param("storyId") Long storyId);



}
