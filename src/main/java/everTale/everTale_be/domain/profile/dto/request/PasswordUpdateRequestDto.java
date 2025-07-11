package everTale.everTale_be.domain.profile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "비밀번호 수정 요청 DTO")
public class PasswordUpdateRequestDto {
    @NotBlank
    @Schema(description = "현재 비밀번호", example = "oldPassword123!")
    private String currentPassword;

    @NotBlank
    @Schema(description = "새 비밀번호", example = "newPassword123!")
    private String newPassword;

    @NotBlank
    @Schema(description = "비밀번호 확인", example = "newPassword123!")
    private String confirmPassword;

    // 비밀번호 일치 여부 확인
    public boolean isPasswordMatch() {
        return this.newPassword.equals(this.confirmPassword);
    }
}
