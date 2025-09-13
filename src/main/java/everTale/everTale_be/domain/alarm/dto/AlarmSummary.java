package everTale.everTale_be.domain.alarm.dto;

import everTale.everTale_be.domain.alarm.entity.Alarm;
import everTale.everTale_be.domain.alarm.entity.Enum.AlarmType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "알림 요약 정보")
public class AlarmSummary {

    @Schema(description = "알림 ID", example = "1")
    private Long alarmId;

    @Schema(description = "알림 타입", example = "EASTEREGG_VOICE")
    private AlarmType alarmType;

    @Schema(description = "스토리 ID", example = "12")
    private Long storyId;

    @Schema(description = "알림 메시지", example = "「용과 마음의 열쇠」 ― 숨은 메세지가 도착했어요. 지금 확인해보세요!")
    private String message;

    @Schema(description = "읽음 여부", example = "0")
    private boolean isRead;

    public static AlarmSummary from(Alarm alarm){
        String message;
        String storyTitle = alarm.getStory().getTitle();

        if (alarm.getAlarmType() == AlarmType.EASTEREGG_VOICE) {
            message = String.format("「%s」 ― 숨은 메세지가 도착했어요. 지금 확인해보세요!", storyTitle);
        } else {
            message = String.format("「%s」 ― 사랑의 편지가 도착했어요. 지금 확인해보세요!", storyTitle);
        }

        return AlarmSummary.builder()
                .alarmId(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .storyId(alarm.getStory().getId())
                .message(message)
                .isRead(alarm.isRead())
                .build();
    }
}
