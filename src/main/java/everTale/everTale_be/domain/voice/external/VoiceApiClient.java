package everTale.everTale_be.domain.voice.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import everTale.everTale_be.global.utils.MultipartInputStreamFileResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoiceApiClient {

    @Value("${ai.base-url}")
    private String fastApiBaseUrl;

    private final RestTemplate restTemplate;

    public String callFastApiToRegisterVoice(MultipartFile file, String voiceName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
            body.add("voice_name", voiceName);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<FastApiVoiceResponseDto> response = restTemplate.postForEntity(
                    fastApiBaseUrl+"/ai/voice/register",
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
                    fastApiBaseUrl+"/ai/voice/play",
                    requestEntity,
                    byte[].class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();  // 파일 저장 X
            } else {
                throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_VOICE);
            }

        } catch (Exception e) {
            throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_VOICE);
        }
    }

    public void callFastApiToDeleteVoice(String voiceKey) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("voice_key", voiceKey);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    fastApiBaseUrl+"/ai/voice/delete",
                    requestEntity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new BadRequestHandler(ErrorStatus.ENABLE_TO_DELETE_VOICE);
            }

        } catch (Exception e) {
            throw new BadRequestHandler(ErrorStatus.ENABLE_TO_DELETE_VOICE);
        }
    }

    @lombok.Data
    static class FastApiVoiceResponseDto {
        @JsonProperty("voice_id")
        private String key;
    }
}
