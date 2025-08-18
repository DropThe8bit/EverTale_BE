package everTale.everTale_be.domain.easterEgg.external;

import everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.response.YoloDetectionResponseDto;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class YoloApiClient {

    @Value("${ai.base-url}")
    private String fastApiBaseUrl;

    private final RestTemplate restTemplate;

    public YoloDetectionResponseDto callFastApiToDetectObject(List<String> imageUrls) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("image_urls", imageUrls);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<YoloDetectionResponseDto> response = restTemplate.postForEntity(
                    fastApiBaseUrl + "/ai/yolo",
                    requestEntity,
                    YoloDetectionResponseDto.class
            );
            return response.getBody();

        } catch (Exception e) {
            throw new BadRequestHandler(ErrorStatus.OBJECT_NOT_DETECTED);
        }
    }
}
