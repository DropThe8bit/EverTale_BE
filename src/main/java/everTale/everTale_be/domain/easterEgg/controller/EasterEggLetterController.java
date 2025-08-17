package everTale.everTale_be.domain.easterEgg.controller;

import everTale.everTale_be.domain.easterEgg.dto.easterEggLetter.EasterEggLetterRequestDTO;
import everTale.everTale_be.domain.easterEgg.dto.easterEggLetter.EasterEggLetterResponseDTO;
import everTale.everTale_be.domain.easterEgg.dto.easterEggLetter.EasterEggLetterStoriesResponseDto;
import everTale.everTale_be.domain.easterEgg.service.EasterEggLetterService;
import everTale.everTale_be.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eastereggs")
@Tag(name = "EasterEggLetter", description = "이스터에그 편지 관리 API")
@RequiredArgsConstructor
public class EasterEggLetterController {

    private final EasterEggLetterService easterEggLetterService;

    @Operation(summary = "이스터에그 편지 생성 API", description = "storyId를 받아 해당 스토리의 이스터에그 편지를 저장한다.")
    @PostMapping("/{storyId}/letters")
    public ApiResponse<String> saveEasterEggLetter(
            @Parameter(description = "스토리 ID") @PathVariable Long storyId,
            @Valid @RequestBody EasterEggLetterRequestDTO.EasterEggLetterCreateRequestDTO request
    ) {
        easterEggLetterService.saveEasterEggLetter(storyId, request);
        return ApiResponse.onSuccess("이스터에그 편지가 성공적으로 저장되었습니다.");
    }


    @Operation(summary = "이스터에그 편지 조회 API", description = "스토리 ID에 해당하는 이스터에그 편지 내용을 조회한다.")
    @GetMapping("/{storyId}/letters")
    public ApiResponse<EasterEggLetterResponseDTO> getEasterEggLetter(
            @Parameter(description = "스토리 ID") @PathVariable Long storyId
    ) {
        EasterEggLetterResponseDTO letterDto = easterEggLetterService.getEasterEggLetterFor(storyId);
        return ApiResponse.onSuccess(letterDto);
    }

    @Operation(summary = "이스터에그 편지 수정 API", description = "스토리 ID를 기준으로 이스터에그 편지를 수정한다.")
    @PutMapping("/{storyId}/letters")
    public ApiResponse<String> updateEasterEggLetter(
            @Parameter(description = "스토리 ID") @PathVariable Long storyId,
            @Valid @RequestBody EasterEggLetterRequestDTO.EasterEggLetterUpdateRequestDTO request
    ) {
        easterEggLetterService.updateEasterEggLetter(storyId, request);
        return ApiResponse.onSuccess("이스터에그 편지가 성공적으로 수정되었습니다.");
    }


    @Operation(summary = "이스터에그 편지 삭제 API", description = "스토리 ID를 기준으로 해당 스토리의 이스터에그 편지를 삭제한다.")
    @DeleteMapping("/{storyId}/letters")
    public ApiResponse<String> deleteEasterEggLetter(
            @Parameter(description = "스토리 ID") @PathVariable Long storyId
    ) {
        easterEggLetterService.deleteEasterEggLetter(storyId);
        return ApiResponse.onSuccess("이스터에그 편지가 성공적으로 삭제되었습니다.");
    }

    @Operation(summary = "이스터에그 편지 책장 조회", description = "선택한 아이 프로필에서 이스터에그를 만들 수 있는 스토리 리스트를 반환합니다.")
    @GetMapping("/letters/{profileId}/created")
    public ApiResponse<EasterEggLetterStoriesResponseDto> getStoriesWithEasterEggVoice(@Parameter(description = "자녀 프로필 ID") @PathVariable Long profileId,
                                                                                       @PageableDefault(size = 4) Pageable pageable){
        EasterEggLetterStoriesResponseDto responseDto = easterEggLetterService.getStoriesWithEasterEggLetter(profileId, pageable);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(summary = "이스터에그 편지 책장 조회", description = "선택한 아이 프로필에서 이스터에그가 이미 만들어진 스토리 리스트를 반환합니다.")
    @GetMapping("/letters/{profileId}/creatable")
    public ApiResponse<EasterEggLetterStoriesResponseDto> getStoriesWithoutEasterEggVoice(@Parameter(description = "자녀 프로필 ID") @PathVariable Long profileId,
                                                                                          @PageableDefault(size = 4) Pageable pageable){
        EasterEggLetterStoriesResponseDto responseDto = easterEggLetterService.getStoriesWithoutEasterEggLetter(profileId, pageable);
        return ApiResponse.onSuccess(responseDto);
    }
}
