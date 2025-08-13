package everTale.everTale_be.domain.easterEggLetter.dto;

import everTale.everTale_be.domain.easterEggLetter.entity.EasterEggLetter;
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
