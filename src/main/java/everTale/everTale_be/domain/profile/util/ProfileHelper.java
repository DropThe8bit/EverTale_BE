package everTale.everTale_be.domain.profile.util;

import everTale.everTale_be.domain.profile.entity.Profile;
import everTale.everTale_be.domain.profile.repository.ProfileRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileHelper {

    private final ProfileRepository profileRepository;

    // 프로필용 메서드
    public Profile getAuthenticatedProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnAuthorizedHandler(ErrorStatus._UNAUTHORIZED);
        }

        if (authentication.getPrincipal() instanceof CustomProfileDetails profileDetails) {
            Long profileId = profileDetails.getProfileId();
            return profileRepository.findById(profileId)
                    .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_PROFILE));
        }
        throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
    }

    // 프로필 ID 반환용 메서드
    public Long getAuthenticatedProfileId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnAuthorizedHandler(ErrorStatus._UNAUTHORIZED);
        }

        if (authentication.getPrincipal() instanceof CustomProfileDetails profileDetails) {
            return profileDetails.getProfileId();
        }
        throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
    }
}