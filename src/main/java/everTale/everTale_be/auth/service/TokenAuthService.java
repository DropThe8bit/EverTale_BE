package everTale.everTale_be.auth.service;

import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenAuthService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "refreshToken:";

    // token 저장
    public void saveRefreshToken(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set(
                PREFIX + userId,   // Key
                refreshToken,           // Value
                7, TimeUnit.DAYS        // 유효기간
        );
    }

    // token 조회
    public String getRefreshToken(Long userId) {
        String token = redisTemplate.opsForValue().get(PREFIX + userId);
        if (token == null) {
            throw new UnAuthorizedHandler(ErrorStatus.NOT_FOUND_REFRESH_TOKEN);
        }
        return token;
    }

    // token 삭제
    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete(PREFIX + userId);
    }

    // token 존재 여부
    public boolean existsRefreshToken(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + userId));
    }
}
