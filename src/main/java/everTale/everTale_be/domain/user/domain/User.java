package everTale.everTale_be.domain.user.domain;

import everTale.everTale_be.domain.profile.domain.Profile;
import everTale.everTale_be.domain.user.domain.Enum.LoginProvider;
import everTale.everTale_be.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "user",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_email_provider",
                columnNames = {"email", "login_provider"}
        )
)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    private String password;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_provider", nullable = false)
    private LoginProvider loginProvider;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Profile> profiles = new ArrayList<>();
//
//    @OneToMany(mappedBy = "voice", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Voice> voices = new ArrayList<>();

    @Builder
    public User(String username,
                String email,
                String password,
                String phone,
                LoginProvider loginProvider) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.loginProvider = loginProvider;
    }

    public void changePassword(String password) {
        this.password = password;
    }
}
