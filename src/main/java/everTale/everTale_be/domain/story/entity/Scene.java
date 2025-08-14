package everTale.everTale_be.domain.story.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import everTale.everTale_be.domain.easterEgg.entity.EasterEggVoice;
import everTale.everTale_be.domain.quiz.entity.Quiz;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Scene {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int page;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @OneToOne(mappedBy = "scene", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Quiz quiz;

    @OneToOne(mappedBy = "scene", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private EasterEggVoice easterEggVoice;

    void setStoryInternal(Story story) {
        this.story = story;
    }
    public void updateContent(String content) {this.content = content;}
    public void updateImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        if (quiz != null && quiz.getScene() != this) {
            quiz.setScene(this);
        }
    }

    public void setEasterEggVoice(EasterEggVoice voice) {
        this.easterEggVoice = voice;
        if (voice != null && voice.getScene() != this) {
            voice.setScene(this);
        }
    }
}

