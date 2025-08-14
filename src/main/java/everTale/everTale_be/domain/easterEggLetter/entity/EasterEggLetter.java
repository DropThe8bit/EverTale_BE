package everTale.everTale_be.domain.easterEggLetter.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import everTale.everTale_be.domain.story.entity.Story;
import everTale.everTale_be.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EasterEggLetter extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300)
    private String content;

    private int imageNum;

    @OneToOne
    @JoinColumn(name = "story_id")
    @JsonBackReference
    private Story story;

    private LocalDate availableAt;

    public void updateStory(Story story) {
        this.story = story;
    }

    public void updateLetter(String content, int imageUrl, LocalDate availableAt) {
        this.content = content;
        this.imageNum = imageUrl;
        this.availableAt = availableAt;
    }

}
