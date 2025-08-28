package everTale.everTale_be.domain.easterEgg.service;

import everTale.everTale_be.domain.alarm.entity.Enum.AlarmType;
import everTale.everTale_be.domain.alarm.service.AlarmService;
import everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.request.EasterEggVoiceRegisterRequestDto;
import everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.request.EasterEggVoiceRequestDto;
import everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.response.EasterEggVoiceStoriesResponseDto;
import everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.response.YoloDetectionResponseDto;
import everTale.everTale_be.domain.easterEgg.entity.EasterEggVoice;
import everTale.everTale_be.domain.easterEgg.external.YoloApiClient;
import everTale.everTale_be.domain.easterEgg.repository.EasterEggVoiceRepository;
import everTale.everTale_be.domain.profile.entity.Profile;
import everTale.everTale_be.domain.profile.service.ProfileService;
import everTale.everTale_be.domain.profile.util.ProfileHelper;
import everTale.everTale_be.domain.story.entity.Scene;
import everTale.everTale_be.domain.story.entity.Story;
import everTale.everTale_be.domain.story.repository.SceneRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import everTale.everTale_be.global.s3.S3Manager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EasterEggVoiceService {

    private final ProfileHelper profileHelper;
    private final S3Manager s3Manager;
    private final SceneRepository sceneRepository;
    private final EasterEggVoiceRepository easterEggVoiceRepository;
    private final AlarmService alarmService;
    private final ProfileService profileService;
    private final YoloApiClient yoloApiClient;

    @Transactional(readOnly = true)
    public YoloDetectionResponseDto getObjectFromImages(Long storyId){
        List<Scene> scenes = sceneRepository.findByStoryIdOrderByPageAsc(storyId);
        List<String> sceneImages = scenes.stream()
                .map(Scene::getImageUrl)
                .toList();
        return yoloApiClient.callFastApiToDetectObject(sceneImages);
    }

    @Transactional
    public void createEasterEggVoice(Long storyId, MultipartFile voiceFile, EasterEggVoiceRegisterRequestDto requestDto){
        Scene scene = sceneRepository.findByStoryIdAndPage(storyId, requestDto.getIndex())
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.SCENE_NOT_FOUND));
        if(easterEggVoiceRepository.existsByScene(scene)){
            throw new BadRequestHandler(ErrorStatus.EASTER_EGG_VOICE_ALREADY_EXISTS);
        }
        Story story = scene.getStory();
        Profile profile = story.getProfile();

        String voiceUrl = s3Manager.uploadFile(voiceFile, "eastereggs/audios");
        EasterEggVoice voice = EasterEggVoice.builder()
                .scene(scene)
                .xCoordinate(requestDto.getDetection().getXCoordinate())
                .yCoordinate(requestDto.getDetection().getYCoordinate())
                .width(requestDto.getDetection().getWidth())
                .height(requestDto.getDetection().getHeight())
                .voiceFile(voiceUrl)
                .build();
        easterEggVoiceRepository.save(voice);
        alarmService.createAlarm(AlarmType.EASTEREGG_VOICE, profile, story);
    }

    @Transactional
    public void deleteEasterEggVoice(Long storyId){
        Profile profile = profileHelper.getAuthenticatedProfile();
        profileService.isParent(profile);

        EasterEggVoice voice = easterEggVoiceRepository.findFirstByScene_Story_Id(storyId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.EASTER_EGG_VOICE_NOT_FOUND));

        Scene scene = voice.getScene();
        scene.setEasterEggVoice(null);
        s3Manager.deleteFile(voice.getVoiceFile());
        easterEggVoiceRepository.delete(voice);
    }

    public EasterEggVoiceStoriesResponseDto getStoriesWithEasterEggVoice(Long profileId, Pageable pageable){
        Profile profile = profileHelper.getAuthenticatedProfile();
        profileService.isParent(profile);
        profileService.validateParentProfileAccess(profile, profileId);

        Page<Story> withVoice = easterEggVoiceRepository.findStoriesWithEasterEggVoice(profileId, pageable);

        return EasterEggVoiceStoriesResponseDto.of(withVoice);
    }

    public EasterEggVoiceStoriesResponseDto getStoriesWithoutEasterEggVoice(Long profileId, Pageable pageable){
        Profile profile = profileHelper.getAuthenticatedProfile();
        profileService.isParent(profile);
        profileService.validateParentProfileAccess(profile, profileId);

        Page<Story> withoutVoice = easterEggVoiceRepository.findStoriesWithoutEasterEggVoice(profileId, pageable);

        return EasterEggVoiceStoriesResponseDto.of(withoutVoice);
    }

    public String getVoiceUrl(Long sceneId, EasterEggVoiceRequestDto requestDto) {
        EasterEggVoice voice = easterEggVoiceRepository.findByScene_Id(sceneId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.EASTER_EGG_VOICE_NOT_FOUND));

        if (!isInsideArea(voice, requestDto.getXCoordinate(), requestDto.getYCoordinate())) {
            return null;
        }
        return voice.getVoiceFile();
    }

    private boolean isInsideArea(EasterEggVoice voice, float clickX, float clickY) {
        float xLeft = voice.getXCoordinate() - voice.getWidth();
        float xRight = voice.getXCoordinate() + voice.getWidth();
        float yTop = voice.getYCoordinate() + voice.getHeight();
        float yBottom = voice.getYCoordinate() - voice.getHeight();

        return clickX >= xLeft && clickX <= xRight && clickY <= yTop && clickY >= yBottom;
    }
}
