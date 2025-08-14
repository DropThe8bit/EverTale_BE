package everTale.everTale_be.domain.story.external;

import everTale.everTale_be.domain.story.dto.StoryRequestDTO;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import everTale.everTale_be.global.utils.MultipartInputStreamFileResource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoryApiClient {

    @Value("${ai.base-url}")
    private String fastApiBaseUrl;

    private final RestTemplate restTemplate;

    // 초기 줄거리 생성
    public String callFastApiForInitStory(StoryRequestDTO.FastApiInitStoryRequestDTO requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StoryRequestDTO.FastApiInitStoryRequestDTO> entity = new HttpEntity<>(requestDto, headers);

        try {
            ResponseEntity<FastApiTextResponseDto> response = restTemplate.postForEntity(
                    fastApiBaseUrl+"/ai/init",
                    entity,
                    FastApiTextResponseDto.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getMessage();
            } else {
                throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_STORY);
            }

        } catch (RestClientException e) {
            log.error("FastAPI 초기 줄거리 요청 실패", e);
            throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_STORY);
        }
    }

    // 다음 줄거리 생성
    public String callFastApiForNextStory(StoryRequestDTO.NextStoryGenerateRequestDTO requestDto) {
        ResponseEntity<FastApiTextResponseDto> response = restTemplate.postForEntity(
                fastApiBaseUrl+"/ai/next-story",
                requestDto,
                FastApiTextResponseDto.class
        );

        if (response.getBody() != null) {
            return response.getBody().getMessage();
        } else {
            throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_STORY);
        }

    }

    // 질문 생성
    public String callFastApiForQuestion(String previousContent) {
        Map<String, String> request = Map.of("previous", previousContent);
        ResponseEntity<FastApiTextResponseDto> response = restTemplate.postForEntity(
                fastApiBaseUrl+"/ai/question",
                request,
                FastApiTextResponseDto.class
        );

        if (response.getBody() != null) {
            return response.getBody().getMessage();
        } else {
            throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_QUESTION);
        }
    }

    // 아이 답변 반영한 다음 줄거리 생성
    public String callFastApiForNextStoryWithAnswer(String previousContent, String answer) {
        Map<String, String> request = Map.of("previous", previousContent, "answer", answer);
        ResponseEntity<FastApiTextResponseDto> response = restTemplate.postForEntity(
                fastApiBaseUrl+"/ai/next-from-answer",
                request,
                FastApiTextResponseDto.class
        );

        if (response.getBody() != null) {
            return response.getBody().getMessage();
        } else {
            throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_STORY);
        }
    }

    // 스케치 이미지 + 프롬프트로 초기 캐릭터 이미지 생성 요청
    public String callFastApiForInitCharacterImageFromSketch(
            MultipartFile sketch, StoryRequestDTO.StoryCharacterInfoRequestDTO request
    ) {
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            body.add("name", request.getCharacterName());
            body.add("age", String.valueOf(request.getAge()));
            body.add("gender", request.getGender());
            body.add("image_description", request.getImageDescription());

            for (String personality : request.getPersonalities()) {
                body.add("personalities", personality);
            }

            try (InputStream inputStream = sketch.getInputStream()) {
                ByteArrayResource sketchResource = new ByteArrayResource(inputStream.readAllBytes()) {
                    @Override
                    public String getFilename() {
                        return sketch.getOriginalFilename();
                    }
                };

                HttpHeaders fileHeaders = new HttpHeaders();
                fileHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                fileHeaders.setContentDispositionFormData("sketch", sketch.getOriginalFilename());

                HttpEntity<Resource> sketchPart = new HttpEntity<>(sketchResource, fileHeaders);
                body.add("sketch", sketchPart);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<FastApiImageResponseDto> response = restTemplate.postForEntity(
                    fastApiBaseUrl+"/ai/init-character-image",
                    requestEntity,
                    FastApiImageResponseDto.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getImage_url();
            } else {
                throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_IMAGE);
            }
        } catch (Exception e) {
            throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_IMAGE);
        }
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
                    fastApiBaseUrl+"/ai/generate-controlnet-image",
                    requestEntity,
                    FastApiImageResponseDto.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getImage_url();
            }else{
                throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_IMAGE);
            }

        } catch (Exception e) {
            throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_IMAGE);
        }
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
                    fastApiBaseUrl+"/ai/generate-dalle-image",
                    requestEntity,
                    FastApiImageResponseDto.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getImage_url();
            }else{
                throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_IMAGE);
            }

        } catch (Exception e) {
            throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_IMAGE);
        }


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
