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

    @Schema(description = "읽음 여부", example = "0")
    private boolean isRead;

    public static AlarmSummary from(Alarm alarm){
        return AlarmSummary.builder()
                .alarmId(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .storyId(alarm.getStory().getId())
                .isRead(alarm.isRead())
                .build();
    }
}
