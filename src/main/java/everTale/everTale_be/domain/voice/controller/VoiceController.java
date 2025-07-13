package everTale.everTale_be.domain.voice.controller;

import everTale.everTale_be.domain.story.service.StoryService;
import everTale.everTale_be.domain.voice.dto.response.VoiceListResponseDto;
import everTale.everTale_be.domain.voice.service.VoiceService;
import everTale.everTale_be.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voices")
@Tag(name = "Voice", description = "ElevenLabs 사용자 음성 API")
public class VoiceController {

    private final StoryService storyService;
    private final VoiceService voiceService;

    @Operation(summary = "등록된 음성 목록 조회", description = "현재 프로필이 속한 루트 사용자의 음성 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<VoiceListResponseDto> getVoicesOfRootUserForProfile(){
        VoiceListResponseDto responseDto = voiceService.getVoicesOfRootUserForProfile();
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(summary = "음성 등록", description = "사용자의 음성 파일을 등록합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> registerVoice(@Parameter(description = "업로드할 음성 파일 (.wav)", required = true)
                                             @RequestPart("voiceFile") MultipartFile voiceFile) {
        voiceService.registerUserVoice(voiceFile);
        return ApiResponse.onSuccess("목소리가 성공적으로 등록되었습니다.");
    }

    @Operation(summary = "텍스트 기반 음성 생성", description = "voiceId와 텍스트를 받아 mp3 음성을 스트리밍으로 반환합니다.")
    @PostMapping("/{voiceId}/stories/{storyId}/scenes/{sceneId}")
    public ResponseEntity<StreamingResponseBody> generateVoice(@PathVariable("voiceId") Long voiceId,
                                                               @PathVariable("storyId") Long storyId,
                                                               @PathVariable("sceneId") Long sceneId) {
        String sceneText = storyService.getSceneText(storyId, sceneId);
        StreamingResponseBody stream = voiceService.generateVoiceStream(voiceId, sceneText);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .body(stream);
    }

    @Operation(summary = "음성 삭제", description = "voiceId에 해당하는 음성을 삭제합니다.")
    @DeleteMapping("/{voiceId}")
    public ApiResponse<String> deleteVoice(@PathVariable Long voiceId) {
        voiceService.deleteVoice(voiceId);
        return ApiResponse.onSuccess("목소리가 성공적으로 삭제되었습니다.");
    }
}
