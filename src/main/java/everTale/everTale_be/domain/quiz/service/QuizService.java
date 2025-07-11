package everTale.everTale_be.domain.quiz.service;

import everTale.everTale_be.domain.profile.domain.Profile;
import everTale.everTale_be.domain.profile.util.UserHelper;
import everTale.everTale_be.domain.quiz.dto.QuizResponseDTO;
import everTale.everTale_be.domain.quiz.entity.Quiz;
import everTale.everTale_be.domain.quiz.entity.enums.Answer;
import everTale.everTale_be.domain.quiz.entity.enums.Badge;
import everTale.everTale_be.domain.quiz.external.QuizApiClient;
import everTale.everTale_be.domain.quiz.repository.QuizRepository;
import everTale.everTale_be.domain.story.entity.Scene;
import everTale.everTale_be.domain.story.repository.SceneRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final SceneRepository sceneRepository;
    private final QuizRepository quizRepository;
    private final QuizApiClient quizApiClient;
    private final UserHelper userHelper;

    @Transactional
    public QuizResponseDTO.QuizGenerateResponseDTO generateQuizForRandomScene(Long storyId) {
        Long profileId = userHelper.getAuthenticatedProfileId();

        List<Scene> scenes = sceneRepository.findAllByStoryIdAndStoryProfileIdAndQuizIsNull(storyId, profileId);
        if (scenes.isEmpty()) {
            throw new NotFoundHandler(ErrorStatus.SCENE_NOT_FOUND);
        }

        Scene scene = scenes.get(ThreadLocalRandom.current().nextInt(scenes.size()));
        QuizApiClient.QuizResponse quizRes = quizApiClient.callFastApiForQuestion(scene.getContent());

        Quiz quiz = Quiz.builder()
                .question(quizRes.getQuestion())
                .option1(quizRes.getOption1())
                .option2(quizRes.getOption2())
                .option3(quizRes.getOption3())
                .option4(quizRes.getOption4())
                .correctAnswer(Answer.valueOf(quizRes.getAnswer()))
                .build();

        scene.setQuiz(quiz);
        sceneRepository.save(scene);

        return QuizResponseDTO.QuizGenerateResponseDTO.from(quiz);
    }

    // quiz 조회
    @Transactional(readOnly = true)
    public List<QuizResponseDTO.QuizGetResponseDTO> getAllQuizzesByStoryId(Long storyId) {
        Long profileId = userHelper.getAuthenticatedProfileId();
        List<Quiz> quizzes = quizRepository.findAllByScene_Story_IdAndScene_Story_Profile_Id(storyId, profileId);

        return quizzes.stream()
                .map(QuizResponseDTO.QuizGetResponseDTO::from)
                .collect(Collectors.toList());
    }

    // quiz 삭제
    @Transactional
    public void deleteAllQuizzesByStoryId(Long storyId) {
        Long profileId = userHelper.getAuthenticatedProfileId();
        quizRepository.deleteByStoryIdAndProfileId(storyId, profileId);
    }

    // quiz 정답
    @Transactional
    public QuizResponseDTO.QuizAnswerResponseDTO submitQuizAnswer(Long quizId, int selectedAnswer) {
        Profile profile = userHelper.getAuthenticatedProfile();
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.QUIZ_NOT_FOUND));

        boolean isCorrect = quiz.checkAndSubmitAnswer(selectedAnswer);

        if (isCorrect) {
            profile.incrementQuizSolvedCount();
            profile.refreshBadge();
        }

        return QuizResponseDTO.QuizAnswerResponseDTO.builder()
                .isCorrect(isCorrect)
                .correctAnswer(quiz.getCorrectAnswer().getNumber())
                .selectedAnswer(selectedAnswer)
                .build();
    }

    // 퀴즈 결과 요약 및 칭호 확인
    @Transactional(readOnly = true)
    public QuizResponseDTO.QuizTitleResponseDTO getQuizzesSummary() {
        Profile profile = userHelper.getAuthenticatedProfile();
        return QuizResponseDTO.QuizTitleResponseDTO.builder()
                .isCorrectCount(profile.getQuizSolvedCount())
                .badge(profile.getBadge().getBadge())
                .build();
    }
}
