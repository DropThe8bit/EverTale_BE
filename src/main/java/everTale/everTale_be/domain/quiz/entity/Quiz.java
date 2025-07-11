package everTale.everTale_be.domain.quiz.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import everTale.everTale_be.domain.quiz.entity.enums.Answer;
import everTale.everTale_be.domain.story.entity.Scene;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String option1;
    private String option2;
    private String option3;
    private String option4;

    @Enumerated(EnumType.STRING)
    private Answer correctAnswer;

    @Enumerated(EnumType.STRING)
    private Answer selectedAnswer;

    @OneToOne
    @JoinColumn(name = "scene_id")
    @JsonBackReference
    private Scene scene;

    boolean solvedCorrectly;

    public void setScene(Scene scene) {
        this.scene = scene;
        if (scene.getQuiz() != this) {
            scene.setQuiz(this);
        }
    }

    public boolean checkAndSubmitAnswer(int selectedAnswer) {
        this.selectedAnswer = Answer.fromNumber(selectedAnswer);
        this.solvedCorrectly = (this.correctAnswer.getNumber() == selectedAnswer);
        return this.solvedCorrectly;
    }
}
