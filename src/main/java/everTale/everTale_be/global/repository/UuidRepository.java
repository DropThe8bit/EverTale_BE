package everTale.everTale_be.global.repository;

import everTale.everTale_be.global.entity.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UuidRepository extends JpaRepository<Uuid, Long> {
    void deleteByUuid(String uuid);

}
