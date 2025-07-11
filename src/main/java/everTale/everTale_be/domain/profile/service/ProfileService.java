package everTale.everTale_be.domain.profile.service;

import everTale.everTale_be.auth.jwt.JwtProvider;
import everTale.everTale_be.auth.jwt.JwtUtil;
import everTale.everTale_be.auth.service.TokenAuthService;
import everTale.everTale_be.domain.profile.domain.Enum.ProfileType;
import everTale.everTale_be.domain.profile.domain.Profile;
import everTale.everTale_be.domain.profile.dto.request.ChildProfileRequestDto;
import everTale.everTale_be.domain.profile.dto.request.ChildProfileUpdateRequestDto;
import everTale.everTale_be.domain.profile.dto.request.ParentProfileUpdateRequestDto;
import everTale.everTale_be.domain.profile.dto.request.PasswordUpdateRequestDto;
import everTale.everTale_be.domain.profile.dto.response.*;
import everTale.everTale_be.domain.profile.repository.ProfileRepository;
import everTale.everTale_be.domain.profile.util.UserHelper;
import everTale.everTale_be.domain.user.domain.User;
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
    private final UserHelper userHelper;

    public void createChildProfile(ChildProfileRequestDto requestDto){
        User rootUser = userHelper.getRootUser();
        boolean exists = profileRepository.existsByUserIdAndName(rootUser.getId(), requestDto.getName());
        if (exists){
            throw new BadRequestHandler(ErrorStatus.ALREADY_EXISTS_PROFILE);
        }
        profileRepository.save(requestDto.toEntity(rootUser, ProfileType.CHILD));
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
                .build();

        profileRepository.save(parentProfile);
    }

    @Transactional(readOnly = true)
    public ProfileListResponseDto getProfiles(){
        Long userId = userHelper.getRootUserId();
        List<Profile> profiles = profileRepository.findAllByUserId(userId);

        return ProfileListResponseDto.from(profiles);
    }

    // 프로필 상세정보 조회
    @Transactional(readOnly = true)
    public ProfileInfoResponseDto getProfileInfo(){
        Profile profile = userHelper.getAuthenticatedProfile();
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
        String newAccessToken = jwtUtil.generateAccessTokenWithProfile(userId, profileId);
        return ProfileEnterResponseDto.from(profile, newAccessToken);
    }

    public void updateChildProfile(ChildProfileUpdateRequestDto requestDto) {
        Profile profile = userHelper.getAuthenticatedProfile();
        profile.updateChild(requestDto);
    }

    public void updateParentProfile(ParentProfileUpdateRequestDto requestDto) {
        Profile profile = userHelper.getAuthenticatedProfile();
        profile.updateParent(requestDto);
    }

    public void updatePassword(PasswordUpdateRequestDto requestDto) {
        Long profileId = userHelper.getAuthenticatedProfileId();
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

    public ProfileEnterResponseDto reissueWithProfile(String refreshToken, Long profileId) {
        Long userId = jwtProvider.getUserIdFromToken(refreshToken);

        String storedRefreshToken = tokenAuthService.getRefreshToken(userId);
        if (!storedRefreshToken.equals(refreshToken)) {
            throw new UnAuthorizedHandler(ErrorStatus.INVALID_REFRESH_TOKEN);
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_PROFILE));
        String newAccessToken = jwtUtil.generateAccessTokenWithProfile(userId, profile.getId());

        return ProfileEnterResponseDto.from(profile, newAccessToken);
    }

    // 프로필 삭제 (회원 탈퇴 X)
    public void deleteProfile(String accessToken){
        Long profileId = userHelper.getAuthenticatedProfileId();

        tokenAuthService.addToBlackListForAccessToken(accessToken, "WITHDRAW");
        profileRepository.anonymizeProfile(profileId);
    }
}
