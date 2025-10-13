package everTale.everTale_be.domain.character.service;

import everTale.everTale_be.domain.character.dto.CharacterCollectionResponseDto;
import everTale.everTale_be.domain.character.dto.CharacterDetailResponseDto;
import everTale.everTale_be.domain.character.entity.StoryCharacter;
import everTale.everTale_be.domain.character.repository.StoryCharacterRepository;
import everTale.everTale_be.domain.profile.entity.Enum.ProfileType;
import everTale.everTale_be.domain.profile.entity.Profile;
import everTale.everTale_be.domain.profile.service.ProfileService;
import everTale.everTale_be.domain.profile.util.ProfileHelper;
import everTale.everTale_be.domain.story.entity.Story;
import everTale.everTale_be.domain.story.service.StoryService;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterService {

    private final ProfileHelper profileHelper;
    private final ProfileService profileService;
    private final StoryService storyService;
    private final StoryCharacterRepository characterRepository;

    // 주인공 모음집 조회
    public CharacterCollectionResponseDto getCharacters(Long profileId, Pageable pageable){
        Profile profile = profileHelper.getAuthenticatedProfile();

        if (profile.getProfileType() == ProfileType.CHILD) {
            profileService.validateChildProfileAccess(profile, profileId);
        } else {
            profileService.validateParentProfileAccess(profile, profileId);
        }

        Page<StoryCharacter> characters = characterRepository.findByStoryProfileId(profileId, pageable);
        return CharacterCollectionResponseDto.from(characters);
    }

    // 주인공 상제정보 조회
    public CharacterDetailResponseDto getCharacterDetail(Long characterId){
        StoryCharacter character = characterRepository.findById(characterId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.CHARACTER_NOT_FOUND));
        Story story = character.getStory();
        Profile author = story.getProfile();
        return CharacterDetailResponseDto.from(character, story, author);
    }
}
