package everTale.everTale_be.domain.story.external;

import everTale.everTale_be.domain.story.dto.StoryRequestDTO;
import everTale.everTale_be.global.utils.MultipartInputStreamFileResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoryApiClient {

    private final RestTemplate restTemplate;

    // 초기 줄거리 생성
    public String callFastApiForInitStory(StoryRequestDTO.FastApiInitStoryRequestDTO requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StoryRequestDTO.FastApiInitStoryRequestDTO> entity = new HttpEntity<>(requestDto, headers);

        try {
            ResponseEntity<FastApiTextResponseDto> response = restTemplate.postForEntity(
                    "http://localhost:8000/ai/init",
                    entity,
                    FastApiTextResponseDto.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getMessage();
            } else {
                return "줄거리 생성 실패 (응답 없음)";
            }

        } catch (RestClientException e) {
            log.error("FastAPI 초기 줄거리 요청 실패", e);
            return "줄거리 생성 실패 (API 호출 에러)";
        }
    }

    // 다음 줄거리 생성
    public String callFastApiForNextStory(StoryRequestDTO.NextStoryGenerateRequestDTO requestDto) {
        ResponseEntity<FastApiTextResponseDto> response = restTemplate.postForEntity(
                "http://localhost:8000/ai/next-story",
                requestDto,
                FastApiTextResponseDto.class
        );

        return response.getBody() != null ? response.getBody().getMessage() : "줄거리 생성 실패";
    }

    // 질문 생성
    public String callFastApiForQuestion(String previousContent) {
        Map<String, String> request = Map.of("previous", previousContent);
        ResponseEntity<FastApiTextResponseDto> response = restTemplate.postForEntity(
                "http://localhost:8000/ai/question",
                request,
                FastApiTextResponseDto.class
        );

        return response.getBody() != null ? response.getBody().getMessage() : "질문 생성 실패";
    }

    // 아이 답변 반영한 다음 줄거리 생성
    public String callFastApiForNextStoryWithAnswer(String previousContent, String answer) {
        Map<String, String> request = Map.of("previous", previousContent, "answer", answer);
        ResponseEntity<FastApiTextResponseDto> response = restTemplate.postForEntity(
                "http://localhost:8000/ai/next-from-answer",
                request,
                FastApiTextResponseDto.class
        );

        return response.getBody() != null ? response.getBody().getMessage() : "다음 줄거리 생성 실패";
    }

    // 스케치 이미지 + 프롬프트로 이미지 생성 요청
    public String callFastApiForImageFromSketch(MultipartFile sketch, String prompt, String genre) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("prompt", prompt);
            body.add("sketch", new MultipartInputStreamFileResource(
                    sketch.getInputStream(), sketch.getOriginalFilename()));
            body.add("genre", genre);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<FastApiImageResponseDto> response = restTemplate.postForEntity(
                    "http://localhost:8000/ai/generate-controlnet-image",
                    requestEntity,
                    FastApiImageResponseDto.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getImage_url();
            }

        } catch (Exception e) {
            log.error("이미지 생성 중 오류 발생", e);
        }

        return "이미지 생성 실패";
    }

    // 프롬프트로 이미지 생성 요청
    public String callFastApiForImageFromPrompt(String prompt, String genre) {
        try {
            // JSON 형태로 만들기
            Map<String, Object> body = new HashMap<>();
            body.put("prompt", prompt);
            body.put("genre", genre);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<FastApiImageResponseDto> response = restTemplate.postForEntity(
                    "http://localhost:8000/ai/generate-dalle-image",
                    requestEntity,
                    FastApiImageResponseDto.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getImage_url();
            }

        } catch (Exception e) {
            log.error("이미지 생성 중 오류 발생", e);
        }

        return "이미지 생성 실패";
    }



    @lombok.Data
    static class FastApiTextResponseDto {
        private String message;
    }

    @lombok.Data
    static class FastApiImageResponseDto {
        private String image_url;
    }
}
