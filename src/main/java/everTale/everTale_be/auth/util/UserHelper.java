package everTale.everTale_be.auth.util;

import everTale.everTale_be.domain.user.entity.User;
import everTale.everTale_be.domain.user.repository.UserRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHelper {

    private final UserRepository userRepository;

    // 루트 유저용 메서드
    public User getRootUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnAuthorizedHandler(ErrorStatus._UNAUTHORIZED);
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            Long userId = userDetails.getUserId();
            return userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));
        }
        throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_USER_ACCESS);
    }

    // 루트 유저 ID만 반환하는 메서드
    public Long getRootUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnAuthorizedHandler(ErrorStatus._UNAUTHORIZED);
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUserId();
        }
        throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_USER_ACCESS);
    }
}
