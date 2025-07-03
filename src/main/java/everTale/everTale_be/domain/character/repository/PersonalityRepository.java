package everTale.everTale_be.domain.character.repository;

import everTale.everTale_be.domain.character.entity.Personality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalityRepository extends JpaRepository<Personality, Long> {
    Optional<Personality> findByPersonality(String personality);

}
