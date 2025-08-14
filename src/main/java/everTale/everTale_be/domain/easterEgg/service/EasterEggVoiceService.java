package everTale.everTale_be.domain.easterEgg.service;

import everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.request.EasterEggVoiceRegisterRequestDto;
import everTale.everTale_be.domain.easterEgg.dto.easterEggVoice.request.EasterEggVoiceRequestDto;
import everTale.everTale_be.domain.easterEgg.entity.EasterEggVoice;
import everTale.everTale_be.domain.easterEgg.repository.EasterEggVoiceRepository;
import everTale.everTale_be.domain.profile.entity.Enum.ProfileType;
import everTale.everTale_be.domain.profile.entity.Profile;
import everTale.everTale_be.domain.profile.util.ProfileHelper;
import everTale.everTale_be.domain.story.entity.Scene;
import everTale.everTale_be.domain.story.repository.SceneRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import everTale.everTale_be.global.s3.S3Manager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class EasterEggVoiceService {

    private final ProfileHelper profileHelper;
    private final S3Manager s3Manager;
    private final SceneRepository sceneRepository;
    private final EasterEggVoiceRepository easterEggVoiceRepository;

    @Transactional
    public void createEasterEggVoice(Long sceneId, MultipartFile voiceFile, EasterEggVoiceRegisterRequestDto requestDto){
        validateParent();
        Scene scene = findScene(sceneId);

        String voiceUrl = s3Manager.uploadFile(voiceFile, "eastereggs/audios");

        EasterEggVoice voice = EasterEggVoice.builder()
                .scene(scene)
                .xCoordinate(requestDto.getXCoordinate())
                .yCoordinate(requestDto.getYCoordinate())
                .width(requestDto.getWidth())
                .height(requestDto.getHeight())
                .voiceFile(voiceUrl)
                .build();
        easterEggVoiceRepository.save(voice);
    }

    @Transactional
    public void deleteEasterEggVoice(Long sceneId){
        validateParent();

        EasterEggVoice voice = easterEggVoiceRepository.findByScene_Id(sceneId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.EASTER_EGG_VOICE_NOT_FOUND));
        Scene scene = voice.getScene();
        scene.setEasterEggVoice(null);
        s3Manager.deleteFile(voice.getVoiceFile());
        easterEggVoiceRepository.delete(voice);
        log.info("삭제 완료: audioId = {}", voice.getId());
    }

    @Transactional(readOnly = true)
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

        log.info("x좌표: {}", clickX);
        log.info("y좌표: {}", clickY);
        log.info("왼쪽 x좌표: {}", xLeft);
        log.info("오른쪽 x좌표: {}", xRight);
        log.info("아래 y좌표: {}", yBottom);
        log.info("위 y좌표: {}", yTop);
        boolean isThere = clickX >= xLeft && clickX <= xRight && clickY <= yTop && clickY >= yBottom;
        log.info("isThere: {}", isThere);
        return clickX >= xLeft && clickX <= xRight && clickY <= yTop && clickY >= yBottom;
    }

    @Transactional(readOnly = true)
    private Scene findScene(Long sceneId){
        return sceneRepository.findById(sceneId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.SCENE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private void validateParent() {
        Profile profile = profileHelper.getAuthenticatedProfile();
        if (profile.getProfileType() != ProfileType.PARENT) {
            throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
        }
    }
}
