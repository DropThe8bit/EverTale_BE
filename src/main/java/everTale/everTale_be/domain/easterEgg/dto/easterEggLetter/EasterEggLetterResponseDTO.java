package everTale.everTale_be.domain.easterEgg.dto.easterEggLetter;

import everTale.everTale_be.domain.easterEgg.entity.EasterEggLetter;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
public class EasterEggLetterResponseDTO {
    private String content;
    private Integer imageNum;
    private LocalDate availableAt;

    public static EasterEggLetterResponseDTO from(EasterEggLetter letter) {
        return EasterEggLetterResponseDTO.builder()
                .content(letter.getContent())
                .imageNum(letter.getImageNum())
                .availableAt(letter.getAvailableAt())
                .build();
    }
}
