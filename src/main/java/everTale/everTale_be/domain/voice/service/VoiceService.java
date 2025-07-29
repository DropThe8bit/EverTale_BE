package everTale.everTale_be.domain.voice.service;

import everTale.everTale_be.domain.profile.entity.Enum.ProfileType;
import everTale.everTale_be.domain.profile.entity.Profile;
import everTale.everTale_be.domain.profile.util.UserHelper;
import everTale.everTale_be.domain.user.entity.User;
import everTale.everTale_be.domain.user.repository.UserRepository;
import everTale.everTale_be.domain.voice.entity.Voice;
import everTale.everTale_be.domain.voice.dto.response.VoiceListResponseDto;
import everTale.everTale_be.domain.voice.external.VoiceApiClient;
import everTale.everTale_be.domain.voice.repository.VoiceRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VoiceService {

    private final UserHelper userHelper;
    private final UserRepository userRepository;
    private final VoiceRepository voiceRepository;
    private final VoiceApiClient voiceApiClient;

    public VoiceListResponseDto getVoicesOfRootUserForProfile() {
        Long profileId = userHelper.getAuthenticatedProfileId();
        User rootUser = findRootUserByProfile(profileId);

        List<Voice> voices = voiceRepository.findAllByUserId(rootUser.getId());
        return VoiceListResponseDto.from(voices);
    }

    public void registerUserVoice(MultipartFile file) {
        validateParent();
        Long profileId = userHelper.getAuthenticatedProfileId();
        User rootUser = findRootUserByProfile(profileId);

        String voiceName = extractNameWithoutExtension(file.getOriginalFilename());
        String key = voiceApiClient.callFastApiToRegisterVoice(file, voiceName);

        Voice voice = Voice.builder()
                .name(voiceName)
                .voiceKey(key)
                .user(rootUser)
                .build();
        voiceRepository.save(voice);
    }

    public StreamingResponseBody generateVoiceStream(Long voiceId, String text) {
        Voice voice = findVoice(voiceId);
        byte[] audioBytes = voiceApiClient.callFastApiForTTS(voice.getVoiceKey(), text);

        return outputStream -> {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(audioBytes)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
        };
    }

    public void deleteVoice(Long voiceId){
        validateParent();
        Long profileId = userHelper.getAuthenticatedProfileId();
        User rootUser = findRootUserByProfile(profileId);
        Voice voice = findVoice(voiceId);

        if (!voice.getUser().getId().equals(rootUser.getId())) {
            throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_USER_ACCESS);
        }
        voiceApiClient.callFastApiToDeleteVoice(voice.getVoiceKey());
        voiceRepository.delete(voice);
    }

    @Transactional(readOnly = true)
    private Voice findVoice(Long voiceId){
        return voiceRepository.findById(voiceId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_VOICE));
    }

    @Transactional(readOnly = true)
    private User findRootUserByProfile(Long profileId){
        return userRepository.findByProfiles_Id(profileId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));
    }

    private void validateParent(){
        Profile profile = userHelper.getAuthenticatedProfile();
        if (profile.getProfileType() != ProfileType.PARENT) {
            throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
        }
    }

    private String extractNameWithoutExtension(String filename) {
        if (filename == null) return "untitled";
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex != -1) ? filename.substring(0, dotIndex) : filename;
    }
}
