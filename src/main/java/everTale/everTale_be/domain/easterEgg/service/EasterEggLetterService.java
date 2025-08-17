package everTale.everTale_be.domain.easterEgg.service;

import everTale.everTale_be.domain.easterEgg.dto.easterEggLetter.EasterEggLetterRequestDTO;
import everTale.everTale_be.domain.easterEgg.dto.easterEggLetter.EasterEggLetterResponseDTO;
import everTale.everTale_be.domain.easterEgg.dto.easterEggLetter.EasterEggLetterStoriesResponseDto;
import everTale.everTale_be.domain.easterEgg.entity.EasterEggLetter;
import everTale.everTale_be.domain.easterEgg.repository.EasterEggLetterRepository;
import everTale.everTale_be.domain.profile.entity.Enum.ProfileType;
import everTale.everTale_be.domain.profile.entity.Profile;
import everTale.everTale_be.domain.profile.service.ProfileService;
import everTale.everTale_be.domain.profile.util.ProfileHelper;
import everTale.everTale_be.domain.story.entity.Story;
import everTale.everTale_be.domain.story.repository.StoryRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor

public class EasterEggLetterService {

    private final StoryRepository storyRepository;
    private final ProfileHelper profileHelper;
    private final EasterEggLetterRepository easterEggLetterRepository;
    private final ProfileService profileService;

    // 이스터에그 편지 생성
    @Transactional
    public void saveEasterEggLetter(Long storyId, EasterEggLetterRequestDTO.EasterEggLetterCreateRequestDTO request) {
        Story story = getStoryForParent(storyId);

        // 이미 편지가 존재하는 경우 예외
        if (story.getEasterEggLetter() != null) {
            throw new BadRequestHandler(ErrorStatus.EASTER_EGG_LETTER_ALREADY_EXISTS);
        }

        // 편지 생성 및 연관관계 설정
        EasterEggLetter letter = EasterEggLetter.builder()
                .content(request.getContent())
                .imageNum(request.getImageNum())
                .availableAt(request.getAvailableAt())
                .build();
        story.addEasterEggLetter(letter);
    }

    // 이스터에그 편지 조회
    @Transactional(readOnly = true)
    public EasterEggLetterResponseDTO getEasterEggLetterFor(Long storyId) {
        Profile profile = profileHelper.getAuthenticatedProfile();
        ProfileType role = profile.getProfileType();

        // 스토리 조회
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.STORY_NOT_FOUND));

        // 권한 확인
        if (role == ProfileType.PARENT) {
            if (!story.getProfile().getUser().getId().equals(profile.getUser().getId())) {
                throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
            }
        } else {
            if (!story.getProfile().getId().equals(profile.getId())) {
                throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
            }
        }

        EasterEggLetter letter = getExistingLetterOrThrow(story);

        // 자녀의 경우 공개 시간 확인
        if (role == ProfileType.CHILD) {
            if (letter.getAvailableAt() != null &&
                    LocalDate.now().isBefore(letter.getAvailableAt())) {
                throw new NotFoundHandler(ErrorStatus.EASTER_EGG_LETTER_NOT_YET_AVAILABLE);
            }
        }

        return EasterEggLetterResponseDTO.from(letter);
    }

    // 이스터에그 편지 수정
    @Transactional
    public void updateEasterEggLetter(Long storyId, EasterEggLetterRequestDTO.EasterEggLetterUpdateRequestDTO request) {
        Story story = getStoryForParent(storyId);
        EasterEggLetter letter = getExistingLetterOrThrow(story);

        // 편지 수정
        letter.updateLetter(request.getContent(), request.getImageNum(), request.getAvailableAt());
    }

    // 이스터에그 편지 삭제
    @Transactional
    public void deleteEasterEggLetter(Long storyId) {
        Story story = getStoryForParent(storyId);
        getExistingLetterOrThrow(story);

        // 연관관계 제거
        story.removeEasterEggLetter();
    }

    private Story getStoryForParent(Long storyId) {
        // 부모 권한 검증
        Profile profile = profileHelper.getAuthenticatedProfile();
        if (profile.getProfileType() != ProfileType.PARENT) {
            throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
        }

        // 스토리 조회
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.STORY_NOT_FOUND));

        // 스토리 소유자 확인
        if (!story.getProfile().getUser().getId().equals(profile.getUser().getId())) {
            throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
        }

        return story;
    }

    private EasterEggLetter getExistingLetterOrThrow(Story story) {
        EasterEggLetter letter = story.getEasterEggLetter();
        if (letter == null) {
            throw new NotFoundHandler(ErrorStatus.EASTER_EGG_LETTER_NOT_FOUND);
        }
        return letter;
    }

    public EasterEggLetterStoriesResponseDto getStoriesWithEasterEggLetter(Long profileId, Pageable pageable){
        Profile profile = profileHelper.getAuthenticatedProfile();
        profileService.isParent(profile);
        profileService.validateParentProfileAccess(profile, profileId);

        Page<Story> withLetter = easterEggLetterRepository.findStoriesWithEasterEggLetter(profileId, pageable);

        return EasterEggLetterStoriesResponseDto.of(withLetter);
    }

    public EasterEggLetterStoriesResponseDto getStoriesWithoutEasterEggLetter(Long profileId, Pageable pageable){
        Profile profile = profileHelper.getAuthenticatedProfile();
        profileService.isParent(profile);
        profileService.validateParentProfileAccess(profile, profileId);

        Page<Story> withoutLetter = easterEggLetterRepository.findStoriesWithoutEasterEggLetter(profileId, pageable);

        return EasterEggLetterStoriesResponseDto.of(withoutLetter);
    }
}


