package everTale.everTale_be.domain.story.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    private String image_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "quiz_id")
    @JsonManagedReference
    private Quiz quiz;

    public void setImageUrl(String imageUrl) {
        this.image_url = imageUrl;
    }

}

