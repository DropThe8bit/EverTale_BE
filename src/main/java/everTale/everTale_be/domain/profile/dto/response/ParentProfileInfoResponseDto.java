package everTale.everTale_be.domain.profile.dto.response;

import everTale.everTale_be.domain.profile.domain.Profile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "부모 프로필 회원 정보 응답 DTO")
public class ParentProfileInfoResponseDto implements ProfileInfoResponseDto{
    @Schema(description = "이름", example = "김부모")
    private String name;

    @Schema(description = "이메일", example = "ewhacse@gmail.com")
    private String email;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;

    public static ParentProfileInfoResponseDto from(Profile profile){
        return ParentProfileInfoResponseDto.builder()
                .name(profile.getName())
                .email(profile.getEmail())
                .phone(profile.getPhone())
                .build();
    }
}
