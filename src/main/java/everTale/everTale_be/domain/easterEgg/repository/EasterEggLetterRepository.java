package everTale.everTale_be.domain.easterEgg.repository;

import everTale.everTale_be.domain.easterEgg.entity.EasterEggLetter;
import everTale.everTale_be.domain.story.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EasterEggLetterRepository extends JpaRepository<EasterEggLetter, Long> {

    @Query(
            value = """
        SELECT DISTINCT s
        FROM Story s
        JOIN s.easterEggLetter el
        WHERE s.profile.id = :profileId
      """,
            countQuery = """
        SELECT COUNT(DISTINCT s.id)
        FROM Story s
        JOIN s.easterEggLetter el
        WHERE s.profile.id = :profileId
      """
    )
    Page<Story> findStoriesWithEasterEggLetter(@Param("profileId") Long profileId, Pageable pageable);

    @Query(
            value = """
        SELECT s
        FROM Story s
        WHERE s.profile.id = :profileId
          AND NOT EXISTS (
            SELECT 1
            FROM EasterEggLetter el
            WHERE el.story = s
          )
      """,
            countQuery = """
        SELECT COUNT(s.id)
        FROM Story s
        WHERE s.profile.id = :profileId
          AND NOT EXISTS (
            SELECT 1
            FROM EasterEggLetter el
            WHERE el.story = s
          )
      """
    )
    Page<Story> findStoriesWithoutEasterEggLetter(@Param("profileId") Long profileId, Pageable pageable);
}
