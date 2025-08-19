package everTale.everTale_be.auth.controller;

import everTale.everTale_be.auth.dto.request.*;
import everTale.everTale_be.auth.dto.response.LoginTokenResponseDto;
import everTale.everTale_be.auth.jwt.JwtUtil;
import everTale.everTale_be.auth.service.AuthService;
import everTale.everTale_be.domain.profile.dto.response.ProfileEnterResponseDto;
import everTale.everTale_be.domain.profile.service.ProfileService;
import everTale.everTale_be.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final ProfileService profileService;

    // 일반 회원가입
    @Operation(summary = "일반 회원가입", description = "이메일, 비밀번호, 이름, 전화번호, 기관명으로 회원가입을 진행합니다.")
    @PostMapping("/signup")
    public ApiResponse<String> signup(@Valid @RequestBody SignUpRequestDto requestDto){
        authService.signup(requestDto);
        return ApiResponse.onSuccess("회원가입이 성공적으로 완료되었습니다.");
    }

    // 일반 로그인
    @Operation(summary = "일반 로그인", description = "이메일과 비밀번호로 로그인하여 토큰을 발급합니다.")
    @PostMapping("/login")
    public ApiResponse<LoginTokenResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto){
        LoginTokenResponseDto responseDto = authService.login(requestDto);
        return ApiResponse.onSuccess(responseDto);
    }

    // 네이버 로그인
    @GetMapping("/naver-login")
    @Operation(summary = "네이버 로그인", description = "네이버 소셜 로그인. OAuth 인가 코드와 state를 통해 토큰을 발급합니다.")
    public ApiResponse<LoginTokenResponseDto> naverLogin(@RequestParam String code,
                                                         @RequestParam String state) {
        LoginTokenResponseDto responseDto = authService.naverLogin(code, state);
        return ApiResponse.onSuccess(responseDto);
    }

    // 로그아웃
    @Operation(summary = "로그아웃", description = "현재 로그인한 사용자의 토큰을 만료 처리합니다.")
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request){
        String accessToken = jwtUtil.extractAccessToken(request);

        authService.logout(accessToken);
        return ApiResponse.onSuccess("로그아웃이 성공적으로 완료되었습니다.");
    }

    // 회원 탈퇴
    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자를 탈퇴 처리합니다. 회원정보가 익명화됩니다.")
    @DeleteMapping("/withdraw")
    public ApiResponse<String> withdraw(HttpServletRequest request){
        String accessToken = jwtUtil.extractAccessToken(request);

        authService.withdraw(accessToken);
        return ApiResponse.onSuccess("회원 탈퇴가 성공적으로 완료되었습니다.");
    }

    @Operation(summary = "프로필 기반 AccessToken 재발급", description = "만료된 access token을 refresh token으로 재발급합니다.")
    @PostMapping("/profiles/reissue")
    public ApiResponse<ProfileEnterResponseDto> reissueProfileAccessToken(@Valid @RequestBody ProfileReissueRequestDto requestDto) {
        ProfileEnterResponseDto responseDto = authService.reissueWithProfile(requestDto.getRefreshToken());
        return ApiResponse.onSuccess(responseDto);
    }

    // 프로필 로그아웃
    @Operation(summary = "프로필 로그아웃", description = "선택한 프로필에서 로그아웃합니다.")
    @PostMapping("/profiles/logout")
    public ApiResponse<String> logoutProfile(HttpServletRequest request){
        String accessToken = jwtUtil.extractAccessToken(request);

        authService.logoutProfile(accessToken);
        return ApiResponse.onSuccess("로그아웃이 성공적으로 완료되었습니다.");
    }

    // 프로필 삭제
    @Operation(summary = "프로필 삭제", description = "선택한 프로필을 삭제합니다.")
    @DeleteMapping("/profiles")
    public ApiResponse<String> deleteProfile(HttpServletRequest request){
        String accessToken = jwtUtil.extractAccessToken(request);

        authService.deleteProfile(accessToken);
        return ApiResponse.onSuccess("프로필이 성공적으로 삭제되었습니다.");
    }
}
