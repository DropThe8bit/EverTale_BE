package everTale.everTale_be.domain.quiz.controller;

import everTale.everTale_be.domain.quiz.dto.QuizResponseDTO;
import everTale.everTale_be.domain.quiz.service.QuizService;
import everTale.everTale_be.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Quiz", description = "퀴즈 관리 API")
public class QuizController {

    private final QuizService quizService;

    @Operation(summary = "랜덤 장면 기반 퀴즈 생성 API", description = "스토리 내 랜덤 장면을 기반으로 4지선다 퀴즈를 생성한다.")
    @PostMapping("/stories/{storyId}/quizzes")
    public ApiResponse<QuizResponseDTO.QuizGenerateResponseDTO> createQuiz(@PathVariable Long storyId) {
        QuizResponseDTO.QuizGenerateResponseDTO result = quizService.generateQuizForRandomScene(storyId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "스토리 기반 퀴즈 전체 조회 API", description = "스토리 ID를 기반으로 해당 스토리의 모든 퀴즈를 조회한다.")
    @GetMapping("/stories/{storyId}/quizzes")
    public ApiResponse<List<QuizResponseDTO.QuizGetResponseDTO>> getQuizzesByStoryId(@PathVariable Long storyId) {
        List<QuizResponseDTO.QuizGetResponseDTO> quizzes = quizService.getAllQuizzesByStoryId(storyId);
        return ApiResponse.onSuccess(quizzes);
    }

    @Operation(summary = "스토리 기반 퀴즈 전체 삭제 API", description = "스토리 ID를 기반으로 해당 스토리의 모든 퀴즈를 삭제한다.")
    @DeleteMapping("/stories/{storyId}/quizzes")
    public ApiResponse<String> deleteQuizzesByStoryId(@PathVariable Long storyId) {
        quizService.deleteAllQuizzesByStoryId(storyId);
        return ApiResponse.onSuccess("해당 스토리의 모든 퀴즈가 삭제되었습니다.");
    }

    @Operation(summary = "퀴즈 정답 제출 API", description = "퀴즈 ID와 선택한 답안을 전송하여 정답 여부를 확인한다.")
    @PostMapping("/quizzes/{quizId}/answer")
    public ApiResponse<QuizResponseDTO.QuizAnswerResponseDTO> submitQuizAnswer(
            @PathVariable Long quizId,
            @RequestParam int selectedAnswer
    ) {
        QuizResponseDTO.QuizAnswerResponseDTO response = quizService.submitQuizAnswer(quizId,selectedAnswer);
        return ApiResponse.onSuccess(response);
    }

    @Operation(summary = "사용자 퀴즈 현황 요약 API", description = "현재 사용자 기준으로 퀴즈 정답 개수 및 칭호를 반환한다.")
    @GetMapping("/quizzes/summary")
    public ApiResponse<QuizResponseDTO.QuizTitleResponseDTO> getQuizzesSummary() {
        QuizResponseDTO.QuizTitleResponseDTO response = quizService.getQuizzesSummary();
        return ApiResponse.onSuccess(response);
    }
}
