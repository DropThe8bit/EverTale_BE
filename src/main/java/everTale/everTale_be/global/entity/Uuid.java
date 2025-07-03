package everTale.everTale_be.global.entity;

import everTale.everTale_be.global.repository.UuidRepository;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Uuid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    private Uuid(String uuid){
        this.uuid = uuid;
    }

    public static Uuid createAndSave(UuidRepository uuidRepository){
        Uuid uuid = new Uuid(java.util.UUID.randomUUID().toString());
        return uuidRepository.save(uuid);

    }
}
