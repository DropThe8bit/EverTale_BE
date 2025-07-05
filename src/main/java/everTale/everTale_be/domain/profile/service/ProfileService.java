package everTale.everTale_be.domain.profile.service;

import everTale.everTale_be.auth.dto.response.LoginTokenResponseDto;
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
import everTale.everTale_be.domain.user.domain.User;
import everTale.everTale_be.domain.user.repository.UserRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenAuthService tokenAuthService;
    private final JwtUtil jwtUtil;

    public void createChildProfile(Long userId, ChildProfileRequestDto requestDto){
        boolean exists = profileRepository.existsByUserIdAndName(userId, requestDto.getName());
        if (exists){
            throw new BadRequestHandler(ErrorStatus.ALREADY_EXISTS_PROFILE);
        }
        User parent = findUser(userId);
        profileRepository.save(requestDto.toEntity(parent, ProfileType.CHILD));
    }

    public void createParentProfile(Long userId) {
        boolean exists = profileRepository.existsByUserIdAndId(userId, 1L);
        if (exists) {
            throw new BadRequestHandler(ErrorStatus.ALREADY_EXISTS_PARENT_PROFILE);
        }
        User user = findUser(userId);

        Profile parentProfile = Profile.builder()
                .name(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .user(user)
                .profileType(ProfileType.PARENT)
                .build();

        profileRepository.save(parentProfile);
    }

    @Transactional(readOnly = true)
    public ProfileListResponseDto getProfiles(Long userId){
        User user = findUser(userId);
        List<Profile> profiles = profileRepository.findAllByUserId(userId);

        return ProfileListResponseDto.from(profiles);
    }

    // 프로필 상세정보 조회
    @Transactional(readOnly = true)
    public ProfileInfoResponseDto getProfileInfo(Long profileId){
        Profile profile = findProfile(profileId);
        if (profile.getProfileType() == ProfileType.PARENT) {
            return ParentProfileInfoResponseDto.from(profile);
        } else {
            return ChildProfileInfoResponseDto.from(profile);
        }
    }

    @Transactional(readOnly = true)
    public ProfileEnterResponseDto enterProfile(Long userId, Long profileId){
        Profile profile = findProfile(profileId);
        if (profile.getUser().getId() != userId) {
            throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
        }
        String newAccessToken = jwtUtil.generateAccessTokenWithProfile(profile.getUser(), profileId);
        return ProfileEnterResponseDto.from(profile, newAccessToken);
    }

    public void updateChildProfile(Long profileId, ChildProfileUpdateRequestDto requestDto) {
        Profile profile = findProfile(profileId);
        profile.updateChild(requestDto);
    }

    public void updateParentProfile(Long profileId, ParentProfileUpdateRequestDto requestDto) {
        Profile profile = findProfile(profileId);
        profile.updateParent(requestDto);
    }

    public void updatePassword(Long profileId, PasswordUpdateRequestDto requestDto) {
        User user = userRepository.findByProfiles_Id(profileId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));
        // 기존 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new UnAuthorizedHandler(ErrorStatus.INVALID_CREDENTIALS);
        }
        // 새 비밀번호와 확인 비밀번호 일치 여부 확인
        if (!requestDto.isPasswordMatch()) {
            throw new BadRequestHandler(ErrorStatus.PASSWORD_MISMATCH);
        }
        user.changePassword(passwordEncoder.encode(requestDto.getNewPassword()));
    }

    public ProfileEnterResponseDto reissueWithProfile(String refreshToken, Long profileId) {
        Claims claims = jwtUtil.extractClaims(refreshToken);
        Long userId = claims.get("userId", Long.class);

        String storedRefreshToken = tokenAuthService.getRefreshToken(userId);
        if (!storedRefreshToken.equals(refreshToken)) {
            throw new UnAuthorizedHandler(ErrorStatus.INVALID_REFRESH_TOKEN);
        }

        User user = findUser(userId);
        Profile profile = findProfile(profileId);

        String newAccessToken = jwtUtil.generateAccessTokenWithProfile(user, profileId);

        return ProfileEnterResponseDto.from(profile, newAccessToken);
    }

    // 프로필 삭제 (회원 탈퇴 X)
    public void deleteProfile(Long profileId){
        Profile profile = findProfile(profileId);
        profileRepository.delete(profile);
    }

    @Transactional(readOnly = true)
    public Profile findProfile(Long profileId){
        return profileRepository.findById(profileId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_PROFILE));
    }

    @Transactional(readOnly = true)
    public User findUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));
    }
}
