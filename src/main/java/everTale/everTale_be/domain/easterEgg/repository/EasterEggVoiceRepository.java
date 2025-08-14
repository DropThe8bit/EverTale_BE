package everTale.everTale_be.domain.easterEgg.repository;

import everTale.everTale_be.domain.easterEgg.entity.EasterEggVoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EasterEggVoiceRepository extends JpaRepository<EasterEggVoice, Long> {

    Optional<EasterEggVoice> findByScene_Id(Long sceneId);
}
