package everTale.everTale_be.domain.profile.repository;

import everTale.everTale_be.domain.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    // 프로필 생성 검증
    boolean existsByUserIdAndName(Long userId, String name);

    // 부모 프로필 생성 검증
    boolean existsByUserIdAndId(Long userId, Long id);

    // 현재 로그인한 회원의 프로필들 가져오기
    List<Profile> findAllByUserId(Long userId);
}
