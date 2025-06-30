package everTale.everTale_be.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "토큰 재발급 요청 DTO")
public class ReissueRequestDto {

    @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI...")
    private String refreshToken;
}
