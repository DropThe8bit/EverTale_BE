package everTale.everTale_be.domain.character.repository;

import everTale.everTale_be.domain.character.entity.StoryCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryCharacterRepository extends JpaRepository<StoryCharacter, Long> {
}
