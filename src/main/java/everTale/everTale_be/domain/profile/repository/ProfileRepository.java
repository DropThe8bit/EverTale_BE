package everTale.everTale_be.domain.profile.repository;

import everTale.everTale_be.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    // 프로필 생성 검증
    boolean existsByUserIdAndName(Long userId, String name);

    // 부모 프로필 생성 검증
    boolean existsByUserIdAndId(Long userId, Long id);

    // 현재 로그인한 회원의 프로필들 가져오기
    List<Profile> findAllByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Profile p SET " +
            "p.name = '알 수 없음', " +
            "p.institution = '알 수 없음', " +
            "p.birth_date = NULL " +
            "WHERE profile_id = :profileId", nativeQuery = true)
    void anonymizeProfile(@Param("profileId") Long profileId);
}
