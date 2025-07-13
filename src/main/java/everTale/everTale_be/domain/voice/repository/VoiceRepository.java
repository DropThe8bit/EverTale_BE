package everTale.everTale_be.domain.voice.repository;

import everTale.everTale_be.domain.voice.domain.Voice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoiceRepository extends JpaRepository<Voice, Long> {
    // 루트 유저의 보유 목소리들 조회
    List<Voice> findAllByUserId(Long userId);
}
