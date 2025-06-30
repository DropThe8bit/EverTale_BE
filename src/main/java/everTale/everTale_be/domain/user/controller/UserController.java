package everTale.everTale_be.domain.user.controller;

import everTale.everTale_be.auth.dto.request.InstitutionRequestDto;
import everTale.everTale_be.auth.jwt.CustomUserDetails;
import everTale.everTale_be.domain.user.service.UserService;
import everTale.everTale_be.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "User", description = "회원 관련 API")
public class UserController {

    private final UserService userService;

    // 기관 정보
    @Operation(summary = "기관 정보 수정", description = "로그인한 사용자의 기관 정보를 수정합니다.")
    @PatchMapping("/institution")
    public ApiResponse<String> updateInstitution(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestBody InstitutionRequestDto request) {
        userService.addAdditionalInfo(userDetails.getUserId(), request);
        return ApiResponse.onSuccess("기관이 성공적으로 변경되었습니다.");
    }
}
