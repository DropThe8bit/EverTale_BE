package everTale.everTale_be.domain.voice.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import everTale.everTale_be.global.utils.MultipartInputStreamFileResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoiceApiClient {

    private final RestTemplate restTemplate;

    public String callFastApiToRegisterVoiceFile(MultipartFile file, String voiceName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
            body.add("voice_name", voiceName);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<FastApiVoiceResponseDto> response = restTemplate.postForEntity(
                    "http://localhost:8000/ai/voice/register",
                    requestEntity,
                    FastApiVoiceResponseDto.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getKey();
            }

            throw new BadRequestHandler(ErrorStatus.ENABLE_TO_REGISTER_VOICE);

        } catch (Exception e) {
            throw new BadRequestHandler(ErrorStatus.ENABLE_TO_REGISTER_VOICE);
        }
    }

    public byte[] callFastApiForTTS(String voiceKey, String text) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("voice_key", voiceKey);
            body.put("text", text);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<byte[]> response = restTemplate.postForEntity(
                    "http://localhost:8000/ai/voice/play",
                    requestEntity,
                    byte[].class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();  // 파일 저장 X
            } else {
                throw new BadRequestHandler(ErrorStatus.UNABLE_TO_GENERATE_VOICE);
            }

        } catch (Exception e) {
            throw new BadRequestHandler(ErrorStatus.UNABLE_TO_GENERATE_VOICE);
        }
    }


    @lombok.Data
    static class FastApiVoiceResponseDto {
        @JsonProperty("voice_id")
        private String key;
    }
}
