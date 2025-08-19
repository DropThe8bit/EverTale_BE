package everTale.everTale_be.domain.easterEgg.repository;

import everTale.everTale_be.domain.easterEgg.entity.EasterEggVoice;
import everTale.everTale_be.domain.story.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EasterEggVoiceRepository extends JpaRepository<EasterEggVoice, Long> {

    Optional<EasterEggVoice> findFirstByScene_Story_Id(Long storyId);

    Optional<EasterEggVoice> findByScene_Id(Long sceneId);

    @Query(
            value = """
            SELECT DISTINCT s
            FROM Story s
            JOIN s.storyScenes sc
            JOIN sc.easterEggVoice ev
            WHERE s.profile.id = :profileId
        """,
            countQuery = """
            SELECT COUNT(DISTINCT s.id)
            FROM Story s
            JOIN s.storyScenes sc
            JOIN sc.easterEggVoice ev
            WHERE s.profile.id = :profileId
        """
    )
    Page<Story> findStoriesWithEasterEggVoice(@Param("profileId") Long profileId, Pageable pageable);

    @Query(
            value = """
            SELECT s
            FROM Story s
            WHERE s.profile.id = :profileId
              AND NOT EXISTS (
                  SELECT 1
                  FROM Scene sc
                  JOIN sc.easterEggVoice ev
                  WHERE sc.story = s
              )
        """,
            countQuery = """
            SELECT COUNT(s.id)
            FROM Story s
            WHERE s.profile.id = :profileId
              AND NOT EXISTS (
                  SELECT 1
                  FROM Scene sc
                  JOIN sc.easterEggVoice ev
                  WHERE sc.story = s
              )
        """
    )
    Page<Story> findStoriesWithoutEasterEggVoice(@Param("profileId") Long profileId, Pageable pageable);
}
