package everTale.everTale_be.domain.profile.controller;

import everTale.everTale_be.auth.jwt.JwtUtil;
import everTale.everTale_be.domain.profile.dto.request.ChildProfileRequestDto;
import everTale.everTale_be.domain.profile.dto.request.ChildProfileUpdateRequestDto;
import everTale.everTale_be.domain.profile.dto.request.ParentProfileUpdateRequestDto;
import everTale.everTale_be.domain.profile.dto.request.PasswordUpdateRequestDto;
import everTale.everTale_be.domain.profile.dto.response.*;
import everTale.everTale_be.domain.profile.service.ProfileService;
import everTale.everTale_be.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
@Tag(name = "Profile", description = "프로필 관리 API")
public class ProfileController {

    private final JwtUtil jwtUtil;
    private final ProfileService profileService;

    // 자녀 프로필 추가
    @Operation(summary = "자녀 프로필 생성", description = "자녀 프로필을 생성합니다.")
    @PostMapping("/child")
    public ApiResponse<String> createChildProfile(@Valid @RequestBody ChildProfileRequestDto requestDto){
        profileService.createChildProfile(requestDto);
        return ApiResponse.onSuccess("프로필이 성공적으로 생성되었습니다.");
    }

    // 회원가입 즉시 프로필 생성 (부모 프로필)
    @Operation(summary = "부모 프로필 생성", description = "회원가입 후 부모 프로필을 생성합니다.")
    @PostMapping("/parent")
    public ApiResponse<String> createParentProfile(){
        profileService.createParentProfile();
        return ApiResponse.onSuccess("프로필이 성공적으로 생성되었습니다.");
    }

    // 프로필 리스트
    @Operation(summary = "프로필 리스트 조회", description = "사용자의 프로필 목록을 조회합니다.")
    @GetMapping("/all")
    public ApiResponse<ProfileListResponseDto> getProfiles(){
        ProfileListResponseDto responseDto = profileService.getProfiles();
        return ApiResponse.onSuccess(responseDto);
    }

    // 자녀 프로필 리스트
    @Operation(summary = "자녀 프로필 리스트 조회", description = "사용자의 자녀 프로필 목록을 조회합니다.")
    @GetMapping("/child")
    public ApiResponse<ProfileListResponseDto> getChildProfiles(){
        ProfileListResponseDto responseDto = profileService.getChildProfiles();
        return ApiResponse.onSuccess(responseDto);
    }

    // 프로필 접속
    @Operation(summary = "프로필 접속", description = "선택한 프로필에 접속합니다.")
    @PostMapping("/{profileId}")
    public ApiResponse<ProfileEnterResponseDto> enterProfile(@Parameter(description = "프로필 ID") @PathVariable("profileId") Long profileId){
        ProfileEnterResponseDto responseDto = profileService.enterProfile(profileId);
        return ApiResponse.onSuccess(responseDto);
    }

    // 프로필(회원 정보) 조회
    @Operation(summary = "프로필 조회", description = "현재 선택된 프로필의 회원 정보를 조회합니다.")
    @GetMapping("/my")
    public ApiResponse<ProfileInfoResponseDto> getProfileInfo(){
        ProfileInfoResponseDto responseDto = profileService.getProfileInfo();
        return ApiResponse.onSuccess(responseDto);
    }

    // 프로필 수정
    @Operation(summary = "자녀 프로필 수정", description = "자녀 프로필 정보를 수정합니다.")
    @PatchMapping("/child")
    public ApiResponse<String> updateChildProfile(@Valid @RequestBody ChildProfileUpdateRequestDto requestDto) {
        profileService.updateChildProfile(requestDto);
        return ApiResponse.onSuccess("회원정보가 성공적으로 수정되었습니다.");
    }

    @Operation(summary = "부모 프로필 수정", description = "부모 프로필 정보를 수정합니다.")
    @PatchMapping("/parent")
    public ApiResponse<String> updateParentProfile(@Valid @RequestBody ParentProfileUpdateRequestDto requestDto) {
        profileService.updateParentProfile(requestDto);
        return ApiResponse.onSuccess("회원정보가 성공적으로 수정되었습니다.");
    }

    @Operation(summary = "비밀번호 수정", description = "현재 비밀번호를 확인 후 새로운 비밀번호로 수정합니다.")
    @PatchMapping("/password")
    public ApiResponse<String> updatePassword(@Valid @RequestBody PasswordUpdateRequestDto requestDto) {
        profileService.updatePassword(requestDto);
        return ApiResponse.onSuccess("비밀번호가 성공적으로 수정되었습니다.");
    }
}
