package everTale.everTale_be.domain.user.domain;

import everTale.everTale_be.domain.user.domain.Enum.LoginProvider;
import everTale.everTale_be.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

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
    private long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    private String password;

    @Column(nullable = false)
    private String phone;

    private String institution;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_provider", nullable = false)
    private LoginProvider loginProvider;

//    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Child> childs = new ArrayList<>();
//
//    @OneToMany(mappedBy = "voice", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Voice> voices = new ArrayList<>();

    @Builder
    public User(String username,
                String email,
                String password,
                String phone,
                String institution,
                LoginProvider loginProvider) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.institution = institution;
        this.loginProvider = loginProvider;
    }

    public void updateInstitution(String institution) {
        this.institution = institution;
    }
}
