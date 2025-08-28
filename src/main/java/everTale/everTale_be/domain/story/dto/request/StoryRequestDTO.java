package everTale.everTale_be.domain.story.dto.request;

import everTale.everTale_be.domain.story.entity.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class StoryRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoryCharacterInfoRequestDTO {

        @Schema(description = "주인공 이름", example = "토토로")
        private String characterName;

        @Schema(description = "주인공 나이", example = "8")
        private int age;

        @Schema(description = "주인공 성별", example = "female")
        private String gender;

        @Schema(description = "주인공 성격 리스트", example = "[\"용감함\", \"씩씩함\"]")
        private List<String> personalities;

        @Schema(description = "주인공 이미지에 대해 설명해주세요", example = "자연을 좋아하는 요정")
        private String imageDescription;

    }

    @Getter
    @Builder
    public static class NextStoryGenerateRequestDTO {

        @Schema(description = "이전 줄거리", example = "여행을 좋아하는 토토로는 숲으로 향했어요. 숲에서는 신비한 친구가 기다리고 있었어요.")
        private String previous;

        @Schema(description = "페이지 번호(1~8)", example = "2")
        private int pageNum;

        @Schema(description = "장르", example = "ADVENTURE")
        private String genre;

        @Schema(description = "주인공 이름", example = "토토로")
        private String name;

        @Schema(description = "주인공 나이", example = "8")
        private int age;

        @Schema(description = "주인공 성별", example = "female")
        private String gender;

        @Schema(description = "주인공 성격 리스트", example = "[\"용감함\", \"씩씩함\"]")
        private List<String> personalities;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoryWorldViewRequestDTO {

        @Schema(description = "동화 장르", example = "ADVENTURE")
        private Genre genre;

        @Schema(description = "세계관 설명", example = "마법이 존재하는 중세 시대의 작은 마을")
        private String worldView;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoryAnswerRequestDTO {
        @Schema(description = "아이답변", example = "비밀을 풀기 위해 별을 모으는 지팡이를 사용했어요.")
        private String answer;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoryUpdateRequestDTO {
        @Schema(description = "수정된 줄거리 내용", example = "주인공은 용기를 내어 드래곤에게 다가갔다.")
        private String updatedContent;
    }
    @Data
    @AllArgsConstructor
    public static class SketchImageRequestDTO {

        @Schema(description = "장면 프롬프트", example = "A little girl holding a balloon")
        private String prompt;

        @Schema(type = "string", format = "binary", description = "스케치 이미지 파일")
        private MultipartFile sketch;
    }

    @Getter
    @Builder
    public static class FastApiInitStoryRequestDTO {
        private String genre;
        private String worldView;
        private String name;
        private int age;
        private String gender;
        private List<String> personalities;
    }



}
