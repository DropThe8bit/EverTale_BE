package everTale.everTale_be.domain.character.service;

import everTale.everTale_be.domain.character.dto.CharacterCollectionResponseDto;
import everTale.everTale_be.domain.character.dto.CharacterDetailResponseDto;
import everTale.everTale_be.domain.character.entity.StoryCharacter;
import everTale.everTale_be.domain.character.repository.StoryCharacterRepository;
import everTale.everTale_be.domain.profile.util.UserHelper;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final UserHelper userHelper;
    private final StoryCharacterRepository characterRepository;

    // 주인공 모음집 조회
    public CharacterCollectionResponseDto getMyCharacters(Pageable pageable){
        Long profileId = userHelper.getAuthenticatedProfileId();
        Page<StoryCharacter> characters = characterRepository.findByStoryProfileId(profileId, pageable);
        return CharacterCollectionResponseDto.from(characters);
    }

    // 주인공 상제정보 조회
    public CharacterDetailResponseDto getCharacterDetail(Long characterId){
        StoryCharacter character = characterRepository.findById(characterId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.CHARACTER_NOT_FOUND));
        return CharacterDetailResponseDto.from(character);
    }
}
