package everTale.everTale_be.domain.character.controller;

import everTale.everTale_be.domain.character.dto.CharacterCollectionResponseDto;
import everTale.everTale_be.domain.character.dto.CharacterDetailResponseDto;
import everTale.everTale_be.domain.character.service.CharacterService;
import everTale.everTale_be.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping
    public ApiResponse<CharacterCollectionResponseDto> getMyCharacters(Pageable pageable){
        CharacterCollectionResponseDto responseDto = characterService.getMyCharacters(pageable);
        return ApiResponse.onSuccess(responseDto);
    }

    @GetMapping("/{characterId}")
    public ApiResponse<CharacterDetailResponseDto> getCharacterDetails(@PathVariable("characterId") Long characterId){
        CharacterDetailResponseDto responseDto = characterService.getCharacterDetail(characterId);
        return ApiResponse.onSuccess(responseDto);
    }
}
