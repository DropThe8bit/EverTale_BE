package everTale.everTale_be.domain.quiz.entity.enums;

import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import lombok.Getter;

@Getter
public enum Answer {
    OPTION1(1), OPTION2(2), OPTION3(3), OPTION4(4);

    private final int number;

    Answer(int number) {
        this.number = number;
    }

    public static Answer fromNumber(int number) {
        for (Answer answer : values()) {
            if (answer.getNumber() == number) {
                return answer;
            }
        }
        throw new BadRequestHandler(ErrorStatus.INVALID_QUIZ_ANSWER_NUM);
    }
}