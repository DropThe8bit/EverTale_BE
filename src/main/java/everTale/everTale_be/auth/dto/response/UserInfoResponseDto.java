package everTale.everTale_be.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "네이버 사용자 정보 응답 DTO")
public class UserInfoResponseDto {

    @JsonProperty("response")
    @Schema(description = "응답 데이터")
    private Response response;

    @Getter
    @NoArgsConstructor
    @Schema(description = "네이버 사용자 정보")
    public static class Response {
        @Schema(description = "네이버 사용자 ID", example = "ABC123DEF456")
        private String id;

        @Schema(description = "이메일", example = "ewhacse@naver.com")
        private String email;

        @Schema(description = "이름", example = "김이화")
        private String name;

        @Schema(description = "전화번호", example = "010-1234-5678")
        private String mobile;
    }
}
