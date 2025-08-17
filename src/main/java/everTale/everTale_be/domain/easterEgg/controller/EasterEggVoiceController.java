package everTale.everTale_be.domain.easterEgg.controller;

import everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.request.EasterEggVoiceRegisterRequestDto;
import everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.request.EasterEggVoiceRequestDto;
import everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.response.EasterEggVoiceStoriesResponseDto;
import everTale.everTale_be.domain.easterEgg.service.EasterEggVoiceService;
import everTale.everTale_be.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eastereggs")
@Tag(name = "EasterEggVoice", description = "이스터 에그 음성 API")
public class EasterEggVoiceController {

    private final EasterEggVoiceService easterEggVoiceService;

    @Operation(summary = "이스터에그 음성 등록", description = "장면에 대한 이스터에그 음성을 업로드합니다.")
    @PostMapping("/scenes/{sceneId}")
    public ApiResponse<String> createEasterEggVoice(@Parameter(description = "장면 ID") @PathVariable("sceneId") Long sceneId,
                                                    @Parameter(description = "업로드할 음성 파일 (.wav)", required = true) @RequestPart("voiceFile") MultipartFile voiceFile,
                                                    @RequestPart EasterEggVoiceRegisterRequestDto requestDto){
        easterEggVoiceService.createEasterEggVoice(sceneId, voiceFile,requestDto);
        return ApiResponse.onSuccess("이스터에그 음성이 성공적으로 등록되었습니다.");
    }

    @Operation(summary = "이스터에그 음성 삭제", description = "해당 장면의 이스터에그 음성을 삭제합니다.")
    @DeleteMapping("/scenes/{sceneId}")
    public ApiResponse<String> deleteEasterEggVoice(@Parameter(description = "장면 ID") @PathVariable("sceneId") Long sceneId){
        easterEggVoiceService.deleteEasterEggVoice(sceneId);
        return ApiResponse.onSuccess("이스터에그 음성이 성공적으로 삭제되었습니다.");
    }

    @Operation(summary = "이스터에그 음성 재생 요청", description = "현재 위치와 크기 정보를 기반으로 해당 장면의 이스터에그 음성 재생 URL을 반환합니다.")
    @PostMapping("/scenes/{sceneId}/play")
    public ApiResponse<String> getEasterEggVoiceUrl(@Parameter(description = "장면 ID") @PathVariable Long sceneId,
                                                    @RequestBody EasterEggVoiceRequestDto requestDto) {
        String voiceUrl = easterEggVoiceService.getVoiceUrl(sceneId, requestDto);
        if (voiceUrl == null) {
            return ApiResponse.onSuccess("이곳에는 음성이 없네요. 다른 장면을 눌러 숨겨진 목소리를 찾아볼까요?");
        }
        return ApiResponse.onSuccess(voiceUrl);
    }

    @Operation(summary = "이스터에그 음성 책장 조회", description = "선택한 아이 프로필에서 이스터에그를 만들 수 있는 스토리 리스트를 반환합니다.")
    @GetMapping("/voices/{profileId}/created")
    public ApiResponse<EasterEggVoiceStoriesResponseDto> getStoriesWithEasterEggVoice(@Parameter(description = "자녀 프로필 ID") @PathVariable Long profileId,
                                                                                      @PageableDefault(size = 4) Pageable pageable){
        EasterEggVoiceStoriesResponseDto responseDto = easterEggVoiceService.getStoriesWithEasterEggVoice(profileId, pageable);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(summary = "이스터에그 음성 책장 조회", description = "선택한 아이 프로필에서 이스터에그가 이미 만들어진 스토리 리스트를 반환합니다.")
    @GetMapping("/voices/{profileId}/creatable")
    public ApiResponse<EasterEggVoiceStoriesResponseDto> getStoriesWithoutEasterEggVoice(@Parameter(description = "자녀 프로필 ID") @PathVariable Long profileId,
                                                                                         @PageableDefault(size = 4) Pageable pageable){
        EasterEggVoiceStoriesResponseDto responseDto = easterEggVoiceService.getStoriesWithoutEasterEggVoice(profileId, pageable);
        return ApiResponse.onSuccess(responseDto);
    }
}
