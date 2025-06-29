package everTale.everTale_be.domain.user.repository;

import everTale.everTale_be.domain.user.domain.Enum.LoginProvider;
import everTale.everTale_be.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailAndLoginProvider(String email, LoginProvider loginProvider);

    Optional<User> findByEmailAndLoginProvider(String email, LoginProvider loginProvider);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET " +
            "u.email = CONCAT('deleted_', STR(:userId), '_', FUNCTION('UUID')), " +
            "u.username = '알 수 없음', " +
            "u.phone = '알 수 없음' " +
            "WHERE u.userId = :userId")
    void anonymizeUser(@Param("userId") Long userId);
}
