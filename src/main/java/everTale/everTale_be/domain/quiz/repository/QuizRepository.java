package everTale.everTale_be.domain.quiz.repository;

import everTale.everTale_be.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @EntityGraph(attributePaths = {"scene", "scene.story"})
    List<Quiz> findAllByScene_Story_IdAndScene_Story_Profile_Id(Long storyId, Long profileId);

    @Modifying
    void deleteBySceneStoryIdAndSceneStoryProfileId(Long storyId, Long profileId);


}
