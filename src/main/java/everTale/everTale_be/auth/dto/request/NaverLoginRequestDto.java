package everTale.everTale_be.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "네이버 로그인 요청 DTO")
public class NaverLoginRequestDto {
    @Schema(description = "인가 코드", example = "AAAA-BBBB-CCCC")
    private String code;

    @Schema(description = "상태값", example = "XYZ123")
    private String state;
}
