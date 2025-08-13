package everTale.everTale_be.domain.story.controller;

import everTale.everTale_be.domain.story.dto.SceneResponseDTO;
import everTale.everTale_be.domain.story.dto.StoryCollectionResponseDto;
import everTale.everTale_be.domain.story.dto.StoryRequestDTO;
import everTale.everTale_be.domain.story.service.StoryService;
import everTale.everTale_be.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stories")
@Tag(name = "Story", description = "스토리 관련 API")
public class StoryController {

    private final StoryService storyService;

    @Operation(summary = "단일 Scene 조회 API", description = "스토리 ID와 씬 번호를 기반으로 해당 씬의 내용을 조회합니다.")
    @GetMapping("/{storyId}/scenes/{sceneNum}")
    public ApiResponse<SceneResponseDTO> getSceneBySceneNum(
            @PathVariable Long storyId,
            @PathVariable int sceneNum
    ) {
        SceneResponseDTO scene = storyService.getSceneBySceneNum(storyId, sceneNum);
        return ApiResponse.onSuccess(scene);
    }

    @Operation(summary = "줄거리 수정 API", description = "특정 장면의 줄거리를 사용자가 수정한 내용으로 업데이트한다.")
    @PatchMapping("/{storyId}/scenes/{sceneNum}")
    public ApiResponse<String> updateSceneContent(
            @PathVariable Long storyId,
            @PathVariable int sceneNum,
            @RequestBody StoryRequestDTO.StoryUpdateRequestDTO request
    ) {
        String updatedContent = storyService.updateSceneContent(storyId, sceneNum, request.getUpdatedContent());
        return ApiResponse.onSuccess(updatedContent);
    }

    @Operation(summary = "스토리 삭제 API", description = "스토리 ID를 기준으로 해당 스토리 및 모든 연관 Scene을 삭제합니다.")
    @DeleteMapping("/{storyId}")
    public ApiResponse<String> deleteStory(@PathVariable Long storyId) {
        storyService.deleteStoryWithScenes(storyId);
        return ApiResponse.onSuccess("스토리가 성공적으로 삭제되었습니다.");
    }

    @Operation(summary = "새 스토리 생성 (ID만 생성)", description = "빈 스토리를 생성하고, 생성된 storyId를 반환합니다.")
    @PostMapping("/create")
    public ApiResponse<Long> createStory() {
        Long storyId = storyService.createEmptyStory();
        return ApiResponse.onSuccess(storyId);
    }

    @Operation(summary = "초기 캐릭터 생성 API", description = "카테코리 정보를 받아서 초기 캐릭터를 설정한다.")
    @PostMapping(
            value = "/{storyId}/character-info",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<String> createCharacter(
            @PathVariable Long storyId,
            @RequestPart StoryRequestDTO.StoryCharacterInfoRequestDTO request,
            @RequestPart("initCharacterImage") MultipartFile initCharacterImage) {
        storyService.saveInitialCharacterInfo(storyId, request, initCharacterImage);
        return ApiResponse.onSuccess("초기 캐릭터 생성이 완료되었습니다.");
    }

    @Operation(summary = "초기 줄거리 생성 API", description = "storyId로 연관된 캐릭터 정보와 사용자 설정(장르, 세계관)을 바탕으로 초기 줄거리를 생성한다.")
    @PostMapping("/{storyId}/init-story")
    public ApiResponse<String> createInitStory(
            @PathVariable Long storyId,
            @RequestBody StoryRequestDTO.StoryWorldViewRequestDTO request
    ) {
        String initStory = storyService.generateInitScene(storyId, request);
        return ApiResponse.onSuccess(initStory);
    }

    @Operation(summary = "다음 줄거리 생성 API", description = "이전 줄거리를 기반으로 다음 줄거리를 생성한다.")
    @PostMapping("/{storyId}/scenes/{sceneNum}")
    public ApiResponse<String> createNextStory(@PathVariable Long storyId, @PathVariable int sceneNum) {
        String nextStory = storyService.generateNextScene(storyId, sceneNum);
        return ApiResponse.onSuccess(nextStory);
    }

    @Operation(summary = "질문 생성 API", description = "이전 줄거리를 기반으로 아이에게 던질 질문을 생성한다.")
    @PostMapping("/{storyId}/scenes/{sceneNum}/question")
    public ApiResponse<String> createQuestionFromPrevScene(@PathVariable Long storyId, @PathVariable int sceneNum) {
        String question = storyService.generateQuestionFromPreviousScene(storyId, sceneNum);
        return ApiResponse.onSuccess(question);
    }

    @Operation(summary = "답변 기반 다음 줄거리 생성 API", description = "아이의 답변을 기반으로 다음 줄거리를 생성한다.")
    @PostMapping("/{storyId}/scenes/{sceneNum}/next-from-answer")
    public ApiResponse<String> createNextSceneFromAnswer(
            @Parameter(description = "스토리 ID") @PathVariable Long storyId,
            @Parameter(description = "장면 번호") @PathVariable int sceneNum,
            @RequestBody StoryRequestDTO.StoryAnswerRequestDTO request
    ) {
        String nextStory = storyService.generateNextSceneWithAnswer(storyId, sceneNum, request.getAnswer());
        return ApiResponse.onSuccess(nextStory);
    }
    @Operation(summary = "스케치 기반 이미지 생성 API", description = "아이의 스케치 이미지와 줄거리를 기반으로 이미지를 생성합니다.")
    @PostMapping(
            value = "/{storyId}/scenes/{sceneNum}/controlnet",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<String> createImageFromSketch(
            @Parameter(description = "스토리 ID") @PathVariable Long storyId,
            @Parameter(description = "장면 번호") @PathVariable int sceneNum,
            @Parameter(description = "스케치 이미지 파일", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
            @RequestPart("sketch") MultipartFile sketch
    ) {
        String image = storyService.generateImageFromSketch(storyId, sceneNum, sketch);
        return ApiResponse.onSuccess(image);
    }

    @Operation(summary = "줄거리 기반 이미지 생성 API", description = "줄거리 텍스트만을 기반으로 이미지를 생성합니다.")
    @PostMapping( "/{storyId}/scenes/{sceneNum}/dalle")
    public ApiResponse<String> createImageFromPrompt(
            @Parameter(description = "스토리 ID") @PathVariable Long storyId,
            @Parameter(description = "장면 번호") @PathVariable int sceneNum
    ) {
        String image = storyService.generateImageFromPrompt(storyId, sceneNum);
        return ApiResponse.onSuccess(image);
    }

    @Operation(summary = "전체 스토리 목록 조회", description = "모든 작가들의 스토리를 페이징 형식으로 조회합니다.")
    @GetMapping
    public ApiResponse<StoryCollectionResponseDto> getAllStories(@PageableDefault(size = 8) Pageable pageable) {
        StoryCollectionResponseDto responseDto = storyService.getAllStories(pageable);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(summary = "프로필 사용자의 스토리 목록 조회", description = "현재 접속한 프로필 사용자의 스토리를 페이징 형식으로 조회합니다.")
    @GetMapping("/{profileId}")
    public ApiResponse<StoryCollectionResponseDto> getMyStories(@Parameter(description = "프로필 ID") @PathVariable Long profileId,
                                                                @PageableDefault(size = 8) Pageable pageable) {
        StoryCollectionResponseDto responseDto = storyService.getStories(profileId, pageable);
        return ApiResponse.onSuccess(responseDto);
    }
}
