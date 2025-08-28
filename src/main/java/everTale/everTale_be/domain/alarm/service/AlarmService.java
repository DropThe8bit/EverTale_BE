package everTale.everTale_be.domain.alarm.service;

import everTale.everTale_be.domain.alarm.dto.response.AlarmListResponseDto;
import everTale.everTale_be.domain.alarm.entity.Alarm;
import everTale.everTale_be.domain.alarm.entity.Enum.AlarmType;
import everTale.everTale_be.domain.alarm.repository.AlarmRepository;
import everTale.everTale_be.domain.profile.entity.Profile;
import everTale.everTale_be.domain.profile.util.ProfileHelper;
import everTale.everTale_be.domain.story.entity.Story;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final ProfileHelper profileHelper;

    @Transactional
    public void createAlarm(AlarmType alarmType, Profile profile, Story story){
        Alarm alarm = Alarm.builder()
                .alarmType(alarmType)
                .profile(profile)
                .story(story)
                .build();
        alarmRepository.save(alarm);
    }

    public AlarmListResponseDto getAlarms(Pageable pageable){
        Long profileId = profileHelper.getAuthenticatedProfileId();
        Page<Alarm> alarms = alarmRepository.findByProfileId(profileId, pageable);
        return AlarmListResponseDto.from(alarms);
    }

    @Transactional
    public void readAlarm(Long alarmId){
        Long profileId = profileHelper.getAuthenticatedProfileId();

        Alarm alarm = alarmRepository.findByIdAndProfileId(alarmId, profileId)
                .orElseThrow(() -> new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS));
        alarm.setRead();
    }
}
