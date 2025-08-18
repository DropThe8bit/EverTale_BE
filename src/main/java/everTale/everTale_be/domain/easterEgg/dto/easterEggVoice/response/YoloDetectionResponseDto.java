package everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "YOLO 객체 탐지 DTO")
public class YoloDetectionResponseDto {

    @Schema(description = "이미지 번호", example = "3")
    private Integer index;

    @Schema(description = "이미지 url", example = "https://...")
    private String url;

    @Schema(description = "객체 좌표 정보")
    private Detection detection;

    @Getter
    @NoArgsConstructor
    @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.NONE
    )
    public static class Detection {

        @JsonProperty("xCoordinate")
        @Schema(description = "X 좌표", example = "120.5")
        private float xCoordinate;

        @JsonProperty("yCoordinate")
        @Schema(description = "Y 좌표", example = "200.0")
        private float yCoordinate;

        @Schema(description = "좌표 허용 너비", example = "30.0")
        private float width;

        @Schema(description = "좌표 허용 높이", example = "40.0")
        private float height;
    }
}
