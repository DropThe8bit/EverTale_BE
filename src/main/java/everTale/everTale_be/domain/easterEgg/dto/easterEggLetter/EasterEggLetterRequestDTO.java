package everTale.everTale_be.domain.easterEgg.dto.easterEggLetter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

public class EasterEggLetterRequestDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EasterEggLetterCreateRequestDTO {

        @Schema(
                description = "편지 내용",
                example = "토토로야, 항상 널 응원하고 있어! 너의 모험이 즐겁고 안전하길 바래. 사랑해!"
        )
        @NotBlank(message = "편지 내용은 비어 있을 수 없습니다.")
        @Size(max=300, message="편지 내용은 300자를 넘을 수 없습니다.")
        private String content;

        @Schema(
                description = "편지지 번호",
                example = "1"
        )
        @Min(value = 1, message = "편지지 번호는 1 이상이어야 합니다.")
        @Max(value = 6, message = "편지지 번호는 6 이하이어야 합니다.")
        private int imageNum;

        @Schema(
                description = "편지를 열 수 있는 시간 (yyyy-MM-dd)",
                example = "2025-07-01"
        )
        @NotNull(message = "편지를 열 수 있는 시간은 반드시 지정해야 합니다.")
        private LocalDate availableAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EasterEggLetterUpdateRequestDTO {
        @Schema(
                description = "편지 내용",
                example = "사랑하는 토토로야, 항상 널 응원하고 있어! 너의 모험이 즐겁고 안전하길 바래. 사랑해!"
        )
        @NotBlank(message = "편지 내용은 비어 있을 수 없습니다.")
        private String content;

        @Schema(
                description = "편지지 번호",
                example = "2"
        )
        @Min(value = 1, message = "편지지 번호는 1 이상이어야 합니다.")
        @Max(value = 6, message = "편지지 번호는 6 이하이어야 합니다.")
        private int imageNum;

        @Schema(
                description = "편지를 열 수 있는 시간 (yyyy-MM-dd)",
                example = "2025-07-01"
        )
        @NotNull(message = "편지를 열 수 있는 시간은 반드시 지정해야 합니다.")
        private LocalDate availableAt;
    }
}
