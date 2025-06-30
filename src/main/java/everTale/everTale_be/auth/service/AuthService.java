package everTale.everTale_be.auth.service;

import everTale.everTale_be.auth.dto.request.LoginRequestDto;
import everTale.everTale_be.auth.dto.request.SignUpRequestDto;
import everTale.everTale_be.auth.dto.response.LoginTokenResponseDto;
import everTale.everTale_be.auth.dto.response.UserInfoResponseDto;
import everTale.everTale_be.auth.jwt.JwtUtil;
import everTale.everTale_be.domain.user.domain.Enum.LoginProvider;
import everTale.everTale_be.domain.user.domain.User;
import everTale.everTale_be.domain.user.repository.UserRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final TokenAuthService tokenAuthService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final NaverService naverService;

    // 일반 회원가입
    public void signup(SignUpRequestDto requestDto) {
        boolean exists = userRepository.existsByEmailAndLoginProvider(requestDto.getEmail(), LoginProvider.LOCAL);

        if (exists) {
            throw new BadRequestHandler(ErrorStatus.ALREADY_EXISTS_EMAIL);
        }
        User user = User.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .username(requestDto.getUsername())
                .phone(requestDto.getPhone())
                .institution(requestDto.getInstitution())
                .loginProvider(LoginProvider.LOCAL) // 일반 회원가입은 LOCAL
                .build();
        userRepository.save(user);
    }

    // 일반 로그인
    public LoginTokenResponseDto login(LoginRequestDto requestDto){
        User user = userRepository.findByEmailAndLoginProvider(requestDto.getEmail(), LoginProvider.LOCAL)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new UnAuthorizedHandler(ErrorStatus.INVALID_CREDENTIALS);
        }

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        tokenAuthService.saveRefreshToken(user.getUserId(), refreshToken);

        return LoginTokenResponseDto.of(user, accessToken, refreshToken, jwtUtil);
    }

    // 네이버 로그인
    public LoginTokenResponseDto naverLogin(String code, String state) {
        String naverAccessToken = naverService.getNaverAccessToken(code, state);
        UserInfoResponseDto userInfo = naverService.getNaverUser(naverAccessToken);
        User user = findOrCreateUserForNaver(userInfo);

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        tokenAuthService.saveRefreshToken(user.getUserId(), refreshToken);

        return LoginTokenResponseDto.of(user, accessToken, refreshToken, jwtUtil);
    }

    public User findOrCreateUserForNaver(UserInfoResponseDto responseDto){
        return userRepository.findByEmailAndLoginProvider(responseDto.getResponse().getEmail(), LoginProvider.NAVER)
                .orElseGet(()-> createUserForNaver(responseDto));
    }

    public User createUserForNaver(UserInfoResponseDto responseDto){
        User user = User.builder()
                .email(responseDto.getResponse().getEmail())
                .username(responseDto.getResponse().getName())
                .phone(responseDto.getResponse().getMobile())
                .password("")
                .institution(null)
                .loginProvider(LoginProvider.NAVER)
                .build();
        return userRepository.save(user);
    }

    // 토큰 재발급
    public LoginTokenResponseDto reissue(String refreshToken){
        Claims claims = jwtUtil.extractClaims(refreshToken);
        Long userId = claims.get("userId", Long.class);

        String storedRefreshToken = tokenAuthService.getRefreshToken(userId);
        if (!storedRefreshToken.equals(refreshToken)) {
            throw new UnAuthorizedHandler(ErrorStatus.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));
        String newAccessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);

        tokenAuthService.deleteRefreshToken(userId);
        tokenAuthService.saveRefreshToken(userId, newRefreshToken);

        return LoginTokenResponseDto.of(user, newAccessToken, newRefreshToken, jwtUtil);
    }

    // 로그아웃
    public void logout(Long userId) {
        tokenAuthService.deleteRefreshToken(userId);
    }

    // 회원 탈퇴
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));
        userRepository.anonymizeUser(userId);
        tokenAuthService.deleteRefreshToken(userId);
    }
}
