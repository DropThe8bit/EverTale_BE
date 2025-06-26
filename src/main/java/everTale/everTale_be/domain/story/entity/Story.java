package everTale.everTale_be.domain.story.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import everTale.everTale_be.domain.character.entity.StoryCharacter;
import everTale.everTale_be.domain.easterEggLetter.entity.EasterEggLetter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String image_url;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "easter_egg_letter_id")
    @JsonManagedReference
    private EasterEggLetter easterEggLetter;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "story_character_id")
    @JsonManagedReference
    private StoryCharacter storyCharacter;

    @OneToMany(mappedBy = "story", cascade = {CascadeType.ALL})
    private List<Scene> storyScenes = new ArrayList<>();

    public void setStoryCharacter(StoryCharacter storyCharacter) {
        this.storyCharacter = storyCharacter;
        if (storyCharacter.getStory() != this) {
            storyCharacter.setStory(this);
        }
    }

    public void updateTitle(String title) {
        this.title = title;
    }


}
