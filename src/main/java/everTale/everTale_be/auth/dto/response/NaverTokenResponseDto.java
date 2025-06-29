package everTale.everTale_be.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "네이버 로그인 토큰 응답 DTO")
public class NaverTokenResponseDto {

    @JsonProperty("access_token")
    @Schema(description = "Access Token", example = "AAAABBBBCCCC")
    private String accessToken;

    @JsonProperty("refresh_token")
    @Schema(description = "Refresh Token", example = "XXXXYYYYZZZZ")
    private String refreshToken;

    @JsonProperty("token_type")
    @Schema(description = "토큰 타입", example = "bearer")
    private String tokenType;

    @JsonProperty("expires_in")
    @Schema(description = "Access Token 만료 시간 (초 단위)", example = "3600")
    private String expiresIn;
}
