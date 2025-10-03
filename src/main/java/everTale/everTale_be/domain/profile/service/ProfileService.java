package everTale.everTale_be.domain.profile.service;

import everTale.everTale_be.auth.jwt.JwtProvider;
import everTale.everTale_be.auth.jwt.JwtUtil;
import everTale.everTale_be.auth.service.TokenAuthService;
import everTale.everTale_be.auth.util.UserHelper;
import everTale.everTale_be.domain.profile.entity.Enum.ProfileStatus;
import everTale.everTale_be.domain.profile.entity.Enum.ProfileType;
import everTale.everTale_be.domain.profile.entity.Profile;
import everTale.everTale_be.domain.profile.dto.request.ChildProfileRequestDto;
import everTale.everTale_be.domain.profile.dto.request.ChildProfileUpdateRequestDto;
import everTale.everTale_be.domain.profile.dto.request.ParentProfileUpdateRequestDto;
import everTale.everTale_be.domain.profile.dto.request.PasswordUpdateRequestDto;
import everTale.everTale_be.domain.profile.dto.response.*;
import everTale.everTale_be.domain.profile.repository.ProfileRepository;
import everTale.everTale_be.domain.profile.util.ProfileHelper;
import everTale.everTale_be.domain.user.entity.User;
import everTale.everTale_be.domain.user.repository.UserRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenAuthService tokenAuthService;
    private final JwtUtil jwtUtil;
    private final JwtProvider jwtProvider;
    private final ProfileHelper profileHelper;
    private final UserHelper userHelper;

    public void createChildProfile(ChildProfileRequestDto requestDto){
        User rootUser = userHelper.getRootUser();
        boolean exists = profileRepository.existsByUserIdAndName(rootUser.getId(), requestDto.getName());
        if (exists){
            throw new BadRequestHandler(ErrorStatus.ALREADY_EXISTS_PROFILE);
        }
        profileRepository.save(requestDto.toEntity(rootUser, ProfileType.CHILD, ProfileStatus.ACTIVE));
    }

    public void createParentProfile() {
        User rootUser = userHelper.getRootUser();
        boolean exists = profileRepository.existsByUserIdAndId(rootUser.getId(), 1L);
        if (exists) {
            throw new BadRequestHandler(ErrorStatus.ALREADY_EXISTS_PARENT_PROFILE);
        }

        Profile parentProfile = Profile.builder()
                .name(rootUser.getUsername())
                .email(rootUser.getEmail())
                .phone(rootUser.getPhone())
                .user(rootUser)
                .profileType(ProfileType.PARENT)
                .profileStatus(ProfileStatus.ACTIVE)
                .build();

        profileRepository.save(parentProfile);
    }

    @Transactional(readOnly = true)
    public ProfileListResponseDto getProfiles(){
        Long userId = userHelper.getRootUserId();
        List<Profile> profiles = profileRepository.findAllByUserIdAndProfileStatusNot(userId, ProfileStatus.DELETED);

        return ProfileListResponseDto.from(profiles);
    }


    @Transactional(readOnly = true)
    public ProfileListResponseDto getChildProfiles(){
        Long userId = userHelper.getRootUserId();
        List<Profile> profiles = profileRepository.findAllByUserIdAndProfileStatusNotAndProfileType(userId, ProfileStatus.DELETED, ProfileType.CHILD);

        return ProfileListResponseDto.from(profiles);
    }

    @Transactional(readOnly = true)
    public ProfileInfoResponseDto getProfileInfo(){
        Profile profile = profileHelper.getAuthenticatedProfile();
        if (profile.getProfileType() == ProfileType.PARENT) {
            return ParentProfileInfoResponseDto.from(profile);
        } else {
            return ChildProfileInfoResponseDto.from(profile);
        }
    }

    @Transactional(readOnly = true)
    public ProfileEnterResponseDto enterProfile(Long profileId){
        Long userId = userHelper.getRootUserId();
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_PROFILE));
        if (profile.getUser().getId() != userId) {
            throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
        }
        String accessToken = jwtUtil.generateAccessTokenWithProfile(userId, profileId);
        String refreshToken = jwtUtil.generateRefreshTokenWithProfile(userId, profileId);
        tokenAuthService.saveRefreshToken(profileId, refreshToken);

        return ProfileEnterResponseDto.from(profile, accessToken, refreshToken, jwtUtil);
    }

    public void updateChildProfile(ChildProfileUpdateRequestDto requestDto) {
        Profile profile = profileHelper.getAuthenticatedProfile();
        profile.updateChild(requestDto);
    }

    public void updateParentProfile(ParentProfileUpdateRequestDto requestDto) {
        Profile profile = profileHelper.getAuthenticatedProfile();
        profile.updateParent(requestDto);
    }

    public void updatePassword(PasswordUpdateRequestDto requestDto) {
        Long profileId = profileHelper.getAuthenticatedProfileId();
        User rootUser = userRepository.findByProfiles_Id(profileId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));

        // 기존 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), rootUser.getPassword())) {
            throw new UnAuthorizedHandler(ErrorStatus.INVALID_CREDENTIALS);
        }
        // 새 비밀번호와 확인 비밀번호 일치 여부 확인
        if (!requestDto.isPasswordMatch()) {
            throw new BadRequestHandler(ErrorStatus.PASSWORD_MISMATCH);
        }
        rootUser.changePassword(passwordEncoder.encode(requestDto.getNewPassword()));
    }

    @Transactional(readOnly = true)
    public void isParent(Profile profile) {
        if (profile.getProfileType() != ProfileType.PARENT) {
            throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
        }
    }

    @Transactional(readOnly = true)
    public void validateChildProfileAccess(Profile profile, Long profileId) {
        if (!profile.getId().equals(profileId)) {
            throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
        }
    }

    @Transactional(readOnly = true)
    public void validateParentProfileAccess(Profile parent, Long profileId) {
        boolean isMyChild = profileRepository.existsByUserIdAndId(parent.getUser().getId(), profileId);
        if (!isMyChild) {
            throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
        }
    }
}
