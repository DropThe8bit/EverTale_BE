package everTale.everTale_be.domain.profile.util;

import everTale.everTale_be.auth.jwt.CustomUserDetails;
import everTale.everTale_be.domain.profile.domain.CustomProfileDetails;
import everTale.everTale_be.domain.profile.domain.Profile;
import everTale.everTale_be.domain.profile.repository.ProfileRepository;
import everTale.everTale_be.domain.user.domain.User;
import everTale.everTale_be.domain.user.repository.UserRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHelper {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    // 루트 유저용 메서드
    public User getRootUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));
    }

    // 프로필용 메서드
    public Profile getAuthenticatedProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomProfileDetails profileDetails = (CustomProfileDetails) authentication.getPrincipal();

        Long profileId = profileDetails.getProfileId();
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_PROFILE));
    }
}