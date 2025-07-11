package everTale.everTale_be.domain.quiz.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;

@Getter
@RequiredArgsConstructor
public enum Badge {
    SHY_SPROUT(0, "수줍은 새싹 꿈나무"),
    SPARKLING_STAR(3, "반짝이는 호기심 별님"),
    LITTLE_BRAVE(5, "작은 용감이"),
    MAGIC_SEED_KEEPER(8, "마법의 씨앗 지니기"),
    COZY_MAGICIAN(12, "포근한 이야기 마술사"),
    HAPPY_FAIRY(17, "빛나는 행복 요정"),
    DREAM_DRAWER(23, "꿈을 그리는 작가님"),
    GOLDEN_IMAGINATION_GENIUS(30, "황금빛 상상 천재"),
    LEGENDARY_STARLIGHT(40, "전설의 반짝반짝 별빛"),
    WORLD_CHANGING_HERO(50, "세상을 바꾸는 영웅님");

    private final int threshold;
    private final String badge;

    public static Badge fromSolvedCount(int count) {
        return Arrays.stream(Badge.values())
                .sorted(Comparator.comparingInt(Badge::getThreshold).reversed())
                .filter(badge -> count >= badge.threshold)
                .findFirst()
                .orElse(SHY_SPROUT);
    }
}
