package everTale.everTale_be.domain.voice.domain;

import everTale.everTale_be.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Voice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voice_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "voice_key", nullable = false)
    private String voiceKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", updatable = false, nullable = false)
    private User user;
}
