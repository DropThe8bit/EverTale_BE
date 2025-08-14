package everTale.everTale_be.domain.easterEgg.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import everTale.everTale_be.domain.story.entity.Story;
import everTale.everTale_be.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EasterEggLetter extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int imageNum;

    @OneToOne
    @JoinColumn(name = "story_id")
    @JsonBackReference
    private Story story;

    private LocalDateTime availableAt;

    public void updateStory(Story story) {
        this.story = story;
    }

    public void updateLetter(String content, int imageUrl, LocalDateTime availableAt) {
        this.content = content;
        this.imageNum = imageUrl;
        this.availableAt = availableAt;
    }

}
