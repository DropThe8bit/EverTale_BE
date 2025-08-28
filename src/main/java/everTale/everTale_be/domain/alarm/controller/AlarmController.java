package everTale.everTale_be.domain.alarm.controller;

import everTale.everTale_be.domain.alarm.dto.response.AlarmListResponseDto;
import everTale.everTale_be.domain.alarm.service.AlarmService;
import everTale.everTale_be.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
@Tag(name = "Alarm", description = "알림 API")
public class AlarmController {

    private final AlarmService alarmService;

    @Operation(summary = "알림 조회 API", description = "사용자의 알림 목록을 8개씩 조회합니다.")
    @GetMapping
    public ApiResponse<AlarmListResponseDto> getAlarms(@PageableDefault(size = 8) Pageable pageable){
        AlarmListResponseDto responseDto = alarmService.getAlarms(pageable);
        return ApiResponse.onSuccess(responseDto);
    }

    @Operation(summary = "알림 읽음 처리", description = "해당 alarmId에 해당하는 알림을 읽음 처리합니다.")
    @PostMapping("{alarmId}")
    public ApiResponse<String> readAlarm(@Parameter(description = "알림 ID") @PathVariable("alarmId") Long alarmId){
        alarmService.readAlarm(alarmId);
        return ApiResponse.onSuccess("알림 읽음 처리가 되었습니다.");
    }
}
