package everTale.everTale_be.domain.easterEggLetter.entity;

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

    private String imageUrl;

    @OneToOne(mappedBy = "easterEggLetter")
    @JsonBackReference
    private Story story;

    private LocalDateTime availableAt;
}
