package everTale.everTale_be.domain.quiz.external;

import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuizApiClient {

    @Value("${ai.base-url}")
    private String fastApiBaseUrl;

    private final RestTemplate restTemplate;

    public QuizResponse callFastApiForQuestion(String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = Map.of("previous", content);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<QuizResponse> response = restTemplate.postForEntity(
                    fastApiBaseUrl+"/ai/generate-quiz",
                    entity,
                    QuizResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_QUIZ);
            }

        } catch (RestClientException e) {
            log.error("FastAPI 퀴즈 요청 실패", e);
            throw new BadRequestHandler(ErrorStatus.ENABLE_TO_GENERATE_QUIZ);
        }
    }


    @Data
    public static class QuizResponse {
        private String question;
        private String option1;
        private String option2;
        private String option3;
        private String option4;
        private String answer;
    }
}