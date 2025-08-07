package everTale.everTale_be.domain.profile.entity;

import everTale.everTale_be.domain.profile.entity.Enum.ProfileType;
import everTale.everTale_be.domain.profile.dto.request.ChildProfileUpdateRequestDto;
import everTale.everTale_be.domain.profile.dto.request.ParentProfileUpdateRequestDto;
import everTale.everTale_be.domain.quiz.entity.enums.Badge;
import everTale.everTale_be.domain.story.entity.Story;
import everTale.everTale_be.domain.user.entity.User;
import everTale.everTale_be.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    // 퀴즈 칭호 관련
    @Column(name = "quiz_solved_count", nullable = false)
    private int quizSolvedCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Badge badge = Badge.SHY_SPROUT;

    // 자녀용 필드
    @Column(name = "birth_date")
    private LocalDate birthDate;
    private String institution;

    // 부모용 필드
    private String phone;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_type", nullable = false)
    private ProfileType profileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User user;

    @OneToMany(mappedBy = "profile")
    private List<Story> stories = new ArrayList<>();

//    @OneToMany(mappedBy = "alarm", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Alram> alrams = new ArrayList<>();

    @Builder
    public Profile(String name,
                   LocalDate birthDate,
                   String institution,
                   String phone,
                   String email,
                   ProfileType profileType,
                   Badge badge,
                   User user) {
        this.name = name;
        this.birthDate = birthDate;
        this.institution = institution;
        this.phone = phone;
        this.email = email;
        this.profileType = profileType;
        this.user = user;
        this.badge = (badge != null) ? badge : Badge.fromSolvedCount(this.quizSolvedCount);

    }

    public void updateChild(ChildProfileUpdateRequestDto requestDto) {
        this.name = requestDto.getName();
        this.birthDate = requestDto.getBirthDate();
        this.institution = requestDto.getInstitution();
    }

    public void updateParent(ParentProfileUpdateRequestDto dto) {
        this.name = dto.getName();
        this.phone = dto.getPhone();
        this.email = dto.getEmail();
    }

    public void incrementQuizSolvedCount() {
        this.quizSolvedCount++;
    }

    public void refreshBadge() {
        this.badge = Badge.fromSolvedCount(this.quizSolvedCount);
    }
}
