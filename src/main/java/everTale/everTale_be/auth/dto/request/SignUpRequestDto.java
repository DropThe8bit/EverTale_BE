package everTale.everTale_be.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원가입 요청 DTO")
public class SignUpRequestDto {

    @Email
    @NotBlank(message = "이메일은 필수입니다.")
    @Schema(description = "이메일", example = "ewhacse@gmail.com")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 영문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다."
    )
    @Schema(description = "비밀번호", example = "password123!")
    private String password;

    @Size(max = 10)
    @NotBlank(message = "이름은 필수입니다.")
    @Schema(description = "이름", example = "김이화")
    private String username;

    @NotBlank(message = "핸드폰 번호는 필수입니다.")
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;
}
