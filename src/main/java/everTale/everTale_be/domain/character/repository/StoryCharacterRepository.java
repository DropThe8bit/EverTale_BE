package everTale.everTale_be.domain.character.repository;

import everTale.everTale_be.domain.character.entity.StoryCharacter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryCharacterRepository extends JpaRepository<StoryCharacter, Long> {
    // 프로필 유저의 모든 스토리의 주인공 조회
    Page<StoryCharacter> findByStoryProfileId(Long profileId, Pageable pageable);
}
