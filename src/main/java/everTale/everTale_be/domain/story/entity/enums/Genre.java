package everTale.everTale_be.domain.story.entity.enums;

import lombok.Getter;

@Getter
public enum Genre {
    ADVENTURE("모험"),
    FRIENDSHIP("우정"),
    MORAL("교훈도덕"),
    FAMILY("사랑우정");

    private final String korean;

    Genre(String korean) {
        this.korean = korean;
    }

}
