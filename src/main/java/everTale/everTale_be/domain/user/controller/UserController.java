package everTale.everTale_be.domain.user.controller;

import everTale.everTale_be.auth.dto.request.InstitutionRequestDto;
import everTale.everTale_be.auth.jwt.CustomUserDetails;
import everTale.everTale_be.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class UserController {

    private final UserService userService;

    // 기관 정보
    @PatchMapping("/institution")
    public ResponseEntity<Void> addAdditionalInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestBody InstitutionRequestDto request) {
        userService.addAdditionalInfo(userDetails.getUserId(), request);
        return ResponseEntity.noContent().build();
    }
}
