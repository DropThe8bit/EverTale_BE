package everTale.everTale_be.domain.easterEggVoice.controller;

import everTale.everTale_be.domain.easterEggVoice.dto.EasterEggVoiceRegisterRequestDto;
import everTale.everTale_be.domain.easterEggVoice.dto.EasterEggVoiceRequestDto;
import everTale.everTale_be.domain.easterEggVoice.service.EasterEggVoiceService;
import everTale.everTale_be.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eastereggs")
public class EasterEggVoiceController {

    private final EasterEggVoiceService easterEggVoiceService;

    @PostMapping("/scenes/{sceneId}")
    public ApiResponse<String> createEasterEggVoice(@Parameter(description = "장면 ID") @PathVariable("sceneId") Long sceneId,
                                                    @Parameter(description = "업로드할 음성 파일 (.wav)", required = true) @RequestPart("voiceFile") MultipartFile voiceFile,
                                                    @RequestPart EasterEggVoiceRegisterRequestDto requestDto){
        easterEggVoiceService.createEasterEggVoice(sceneId, voiceFile,requestDto);
        return ApiResponse.onSuccess("이스터에그 음성이 성공적으로 등록되었습니다.");
    }

    @DeleteMapping("/scenes/{sceneId}")
    public ApiResponse<String> deleteEasterEggVoice(@Parameter(description = "장면 ID") @PathVariable("sceneId") Long sceneId){
        easterEggVoiceService.deleteEasterEggVoice(sceneId);
        return ApiResponse.onSuccess("이스터에그 음성이 성공적으로 삭제되었습니다.");
    }

    @PostMapping("/scenes/{sceneId}/play")
    public ApiResponse<String> getEasterEggVoiceUrl(@Parameter(description = "장면 ID") @PathVariable Long sceneId,
                                                    @RequestBody EasterEggVoiceRequestDto requestDto) {
        String voiceUrl = easterEggVoiceService.getVoiceUrl(sceneId, requestDto);
        if (voiceUrl == null) {
            return ApiResponse.onSuccess("이곳에는 음성이 없네요. 다른 장면을 눌러 숨겨진 목소리를 찾아볼까요?");
        }
        return ApiResponse.onSuccess(voiceUrl);
    }
}
