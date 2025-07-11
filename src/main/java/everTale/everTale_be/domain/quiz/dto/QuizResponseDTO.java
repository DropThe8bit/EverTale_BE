package everTale.everTale_be.domain.quiz.dto;

import everTale.everTale_be.domain.quiz.entity.Quiz;
import lombok.*;

import java.util.List;


public class QuizResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuizGetResponseDTO {

        private Long quizId;
        private String question;
        private List<String> options;
        private int answer;

        public static QuizGetResponseDTO from(Quiz quiz) {
            return QuizGetResponseDTO.builder()
                    .quizId(quiz.getId())
                    .question(quiz.getQuestion())
                    .options(List.of(
                            quiz.getOption1(),
                            quiz.getOption2(),
                            quiz.getOption3(),
                            quiz.getOption4()
                    ))
                    .answer(quiz.getCorrectAnswer().getNumber())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuizGenerateResponseDTO {
        private Long quizId;
        private String question;
        private List<String> options;
        private int correctAnswer;

        public static QuizGenerateResponseDTO from(Quiz quiz) {
            return QuizGenerateResponseDTO.builder()
                    .quizId(quiz.getId())
                    .question(quiz.getQuestion())
                    .options(List.of(
                            quiz.getOption1(),
                            quiz.getOption2(),
                            quiz.getOption3(),
                            quiz.getOption4()
                    ))
                    .correctAnswer(quiz.getCorrectAnswer().getNumber())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class QuizAnswerResponseDTO {
        private boolean isCorrect;
        private int correctAnswer;
        private int selectedAnswer;
    }

    @Getter
    @Builder
    public static class QuizTitleResponseDTO {
        private int isCorrectCount;
        private String badge;
    }
}

