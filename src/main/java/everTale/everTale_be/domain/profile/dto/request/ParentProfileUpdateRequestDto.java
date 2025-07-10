package everTale.everTale_be.domain.profile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "부모 프로필 수정 요청 DTO")
public class ParentProfileUpdateRequestDto {
    @Schema(description = "부모 이름", example = "김삼화")
    private String name;

    @Schema(description = "부모 전화번호", example = "010-8765-4321")
    private String phone;

    @Schema(description = "부모 이메일", example = "ewhacse@naver.com")
    private String email;
}
