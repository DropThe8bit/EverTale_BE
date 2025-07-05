package everTale.everTale_be.domain.character.entity.mapping;

import everTale.everTale_be.domain.character.entity.Personality;
import everTale.everTale_be.domain.character.entity.StoryCharacter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CharacterPersonality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private StoryCharacter character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personality_id",nullable = false)
    private Personality personality;
}
