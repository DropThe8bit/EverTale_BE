package everTale.everTale_be.domain.alarm.dto.response;

import everTale.everTale_be.domain.alarm.dto.AlarmSummary;
import everTale.everTale_be.domain.alarm.entity.Alarm;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@Schema(description = "알림 목록 응답 DTO")
public class AlarmListResponseDto {

    @Schema(description = "알림 요약 리스트")
    private List<AlarmSummary> alarmSummaries;

    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    private int currentPage;

    @Schema(description = "전체 페이지 수", example = "5")
    private int totalPage;

    @Schema(description = "전체 스토리 개수", example = "123")
    private long totalCount;

    public static AlarmListResponseDto from(Page<Alarm> alarms){
        return AlarmListResponseDto.builder()
                .alarmSummaries(alarms.stream().map(AlarmSummary::from).toList())
                .currentPage(alarms.getNumber())
                .totalPage(alarms.getTotalPages())
                .totalCount(alarms.getTotalElements())
                .build();
    }
}
