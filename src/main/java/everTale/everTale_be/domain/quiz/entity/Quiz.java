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

    private String option_1;
    private String option_2;
    private String option_3;
    private String option_4;

    @Enumerated(EnumType.STRING)
    private Answer answer;

    @OneToOne(mappedBy = "quiz")
    @JsonBackReference
    private Scene scene;

}
