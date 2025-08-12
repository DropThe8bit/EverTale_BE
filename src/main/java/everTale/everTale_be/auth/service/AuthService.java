package everTale.everTale_be.auth.service;

import everTale.everTale_be.auth.dto.request.LoginRequestDto;
import everTale.everTale_be.auth.dto.request.SignUpRequestDto;
import everTale.everTale_be.auth.dto.response.LoginTokenResponseDto;
import everTale.everTale_be.auth.dto.response.UserInfoResponseDto;
import everTale.everTale_be.auth.jwt.JwtProvider;
import everTale.everTale_be.auth.jwt.JwtUtil;
import everTale.everTale_be.auth.util.UserHelper;
import everTale.everTale_be.domain.user.entity.Enum.LoginProvider;
import everTale.everTale_be.domain.user.entity.User;
import everTale.everTale_be.domain.user.repository.UserRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final JwtProvider jwtProvider;
    private final TokenAuthService tokenAuthService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final NaverService naverService;
    private final UserHelper userHelper;

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

        return LoginTokenResponseDto.of(user, accessToken, jwtUtil, false);
    }

    // 네이버 로그인
    public LoginTokenResponseDto naverLogin(String code, String state) {
        String naverAccessToken = naverService.getNaverAccessToken(code, state);
        UserInfoResponseDto userInfo = naverService.getNaverUser(naverAccessToken);

        Optional<User> findUser = userRepository.findByEmailAndLoginProvider(userInfo.getResponse().getEmail(), LoginProvider.NAVER);

        final boolean isFirstLogin;
        final User user;

        if (findUser.isPresent()) {
            user = findUser.get();
            isFirstLogin = false;
        } else {
            user = createUserForNaver(userInfo);
            isFirstLogin = true;
        }

        String accessToken = jwtUtil.generateAccessToken(user);
        return LoginTokenResponseDto.of(user, accessToken, jwtUtil, isFirstLogin);
    }

    public User createUserForNaver(UserInfoResponseDto responseDto){
        User user = User.builder()
                .email(responseDto.getResponse().getEmail())
                .username(responseDto.getResponse().getName())
                .phone(responseDto.getResponse().getMobile())
                .password("")
                .loginProvider(LoginProvider.NAVER)
                .build();
        return userRepository.save(user);
    }

    // 로그아웃
    public void logout(String accessToken) {
        Long userId = userHelper.getRootUserId();
        tokenAuthService.validateNotBlackListed(accessToken);

        tokenAuthService.addToBlackListForAccessToken(accessToken, "LOGOUT");
        tokenAuthService.deleteRefreshToken(userId);
    }

    // 회원 탈퇴
    public void withdraw(String accessToken) {
        Long userId = userHelper.getRootUserId();
        tokenAuthService.validateNotBlackListed(accessToken);

        tokenAuthService.addToBlackListForAccessToken(accessToken, "WITHDRAW");
        tokenAuthService.deleteRefreshToken(userId);
        userRepository.anonymizeUser(userId);
    }
}
