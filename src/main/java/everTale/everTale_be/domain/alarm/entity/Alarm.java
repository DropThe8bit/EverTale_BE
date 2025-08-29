package everTale.everTale_be.domain.alarm.entity;

import everTale.everTale_be.domain.alarm.entity.Enum.AlarmType;
import everTale.everTale_be.domain.profile.entity.Profile;
import everTale.everTale_be.domain.story.entity.Story;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column(nullable = false)
    private boolean isRead = false;

    @CreatedDate
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", updatable = false, nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", updatable = false, nullable = false)
    private Story story;

    @Builder
    public Alarm(AlarmType alarmType,
                 Profile profile,
                 Story story) {
        this.alarmType = alarmType;
        this.profile = profile;
        this.story = story;
    }

    public void setRead(){
        isRead = true;
    }
}
