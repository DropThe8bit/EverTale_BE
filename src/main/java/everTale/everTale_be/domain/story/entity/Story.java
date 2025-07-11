package everTale.everTale_be.domain.story.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import everTale.everTale_be.domain.character.entity.StoryCharacter;
import everTale.everTale_be.domain.easterEggLetter.entity.EasterEggLetter;
import everTale.everTale_be.domain.story.entity.enums.Genre;
import everTale.everTale_be.domain.profile.domain.Profile;
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

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "easter_egg_letter_id")
    @JsonManagedReference
    private EasterEggLetter easterEggLetter;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "character_id")
    @JsonManagedReference
    private StoryCharacter character;

    @Builder.Default
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scene> storyScenes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", updatable = false, nullable = false)
    private Profile profile;

    public void setCharacter(StoryCharacter character) {
        this.character = character;
        if (character.getStory() != this) {
            character.setStory(this);
        }
    }

    public void updateTitle(String title) {
        this.title = title;
    }



    public void updateGenre(Genre genre) {this.genre = genre;}
    public void addScene(Scene scene) {
        storyScenes.add(scene);
        if (scene.getStory() != this) {
            scene.setStoryInternal(this);
        }
    }
}
