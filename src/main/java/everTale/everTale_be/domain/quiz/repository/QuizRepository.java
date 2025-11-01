package everTale.everTale_be.domain.quiz.repository;

import everTale.everTale_be.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @EntityGraph(attributePaths = {"scene", "scene.story"})
    List<Quiz> findAllByScene_Story_Id(Long storyId);

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM Quiz q
    WHERE q.scene.story.id = :storyId
    AND q.scene.story.profile.id = :profileId
    """)
    void deleteByStoryIdAndProfileId(@Param("storyId") Long storyId,
                                     @Param("profileId") Long profileId);


}
