package everTale.everTale_be.domain.alarm.repository;

import everTale.everTale_be.domain.alarm.entity.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Optional<Alarm> findByIdAndProfileId(Long id, Long profileId);

    Page<Alarm> findByProfileId(Long profileId, Pageable pageable);
}
