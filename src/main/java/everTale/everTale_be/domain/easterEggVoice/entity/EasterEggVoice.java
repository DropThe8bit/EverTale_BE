package everTale.everTale_be.domain.easterEggVoice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import everTale.everTale_be.domain.story.entity.Scene;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EasterEggVoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audio_id")
    private Long id;

    @Column(name = "x_coordinate")
    private float xCoordinate;

    @Column(name = "y_coordinate")
    private float yCoordinate;

    private float width;
    private float height;

    @Column(name = "voice_file")
    private String voiceFile;

    @CreatedDate
    @Column(name="created_at", updatable = false, columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "scene_id")
    @JsonBackReference
    private Scene scene;

    @Builder
    public EasterEggVoice(float xCoordinate, float yCoordinate, float width, float height,
                          String voiceFile, Scene scene) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.width = width;
        this.height = height;
        this.voiceFile = voiceFile;
        this.scene = scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        if (scene != null && scene.getEasterEggVoice() != this) {
            scene.setEasterEggVoice(this);
        }
    }
}
