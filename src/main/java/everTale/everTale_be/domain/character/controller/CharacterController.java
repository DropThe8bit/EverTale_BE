package everTale.everTale_be.domain.character.controller;

import everTale.everTale_be.domain.character.dto.CharacterCollectionResponseDto;
import everTale.everTale_be.domain.character.dto.CharacterDetailResponseDto;
import everTale.everTale_be.domain.character.service.CharacterService;
import everTale.everTale_be.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/characters")
@Tag(name = "Character", description = "캐릭터 관련 API")
public class CharacterController {

    private final CharacterService characterService;

    @Operation(summary = "내가 만든 스토리의 캐릭터 목록 조회", description = "내가 작성한 스토리에 등장하는 모든 캐릭터를 페이지네이션 형태로 조회합니다.")
    @GetMapping
    public ApiResponse<CharacterCollectionResponseDto> getMyCharacters(Pageable pageable){
        CharacterCollectionResponseDto responseDto = characterService.getMyCharacters(pageable);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(summary = "캐릭터 상세 조회", description = "캐릭터 ID를 기반으로 해당 캐릭터의 상세 정보를 조회합니다.")
    @GetMapping("/{characterId}")
    public ApiResponse<CharacterDetailResponseDto> getCharacterDetails(@Parameter(description = "캐릭터 ID") @PathVariable("characterId") Long characterId){
        CharacterDetailResponseDto responseDto = characterService.getCharacterDetail(characterId);
        return ApiResponse.onSuccess(responseDto);
    }
}
