package everTale.everTale_be.domain.story.repository;

import everTale.everTale_be.domain.story.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
    // 내가 쓴 스토리 조회
    Page<Story> findByProfileId(Long profileId, Pageable pageable);
}
