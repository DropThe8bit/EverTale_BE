package everTale.everTale_be.auth.service;

import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenAuthService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "refreshToken:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    // token 저장
    public void saveRefreshToken(Long profileId, String refreshToken) {
        redisTemplate.opsForValue().set(
                PREFIX + profileId, // Key
                refreshToken,           // Value
                7, TimeUnit.DAYS        // 유효기간
        );
    }

    // token 조회
    public String getRefreshToken(Long profileId) {
        String token = redisTemplate.opsForValue().get(PREFIX + profileId);
        if (token == null) {
            throw new UnAuthorizedHandler(ErrorStatus.NOT_FOUND_REFRESH_TOKEN);
        }
        return token;
    }

    // token 삭제
    public void deleteRefreshToken(Long profileId) {
        redisTemplate.delete(PREFIX + profileId);
    }

    // token 존재 여부
    public void validateRefreshToken(Long profileId, String refreshToken) {
        String storedRefreshToken = getRefreshToken(profileId);
        if (!storedRefreshToken.equals(refreshToken)) {
            throw new UnAuthorizedHandler(ErrorStatus.INVALID_REFRESH_TOKEN);
        }
    }

    // BlackList
    public void addToBlackListForAccessToken(String accessToken, String reason) {
        log.info("Access Token: {}", accessToken);

        if (accessToken == null || accessToken.isEmpty()) {
            throw new BadRequestHandler(ErrorStatus.EMPTY_ACCESS_TOKEN);
        }
        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + accessToken,
                reason,
                1000 * 60 * 60,
                TimeUnit.MILLISECONDS
        );
    }

    public void validateNotBlackListed(String token) {
        boolean isBlackListed = Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
        if (isBlackListed) {
            throw new BadRequestHandler(ErrorStatus.BLOCKED_TOKEN);
        }
    }
}
