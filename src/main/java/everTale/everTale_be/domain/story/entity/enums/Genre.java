package everTale.everTale_be.domain.story.entity.enums;

import lombok.Getter;

@Getter
public enum Genre {
    ADVENTURE("모험"),
    FRIENDSHIP("우정"),
    LESSON("교훈"),
    LOVE("사랑");

    private final String korean;

    Genre(String korean) {
        this.korean = korean;
    }

}
