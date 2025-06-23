package everTale.everTale_be.domain.character.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import everTale.everTale_be.domain.character.entity.enums.Gender;
import everTale.everTale_be.domain.character.entity.mapping.CharacterPersonality;
import everTale.everTale_be.domain.story.entity.Story;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoryCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String image_url;
    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(mappedBy = "storyCharacter")
    @JsonBackReference
    private Story story;

    @OneToMany(mappedBy = "storyCharacter",cascade = {CascadeType.ALL})
    private List<CharacterPersonality> characterPersonalities = new ArrayList<>();

}
