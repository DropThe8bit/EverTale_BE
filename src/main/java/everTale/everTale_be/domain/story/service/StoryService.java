package everTale.everTale_be.domain.story.service;

import everTale.everTale_be.domain.character.entity.Personality;
import everTale.everTale_be.domain.character.entity.StoryCharacter;
import everTale.everTale_be.domain.character.entity.enums.Gender;
import everTale.everTale_be.domain.character.repository.PersonalityRepository;
import everTale.everTale_be.domain.character.repository.StoryCharacterRepository;
import everTale.everTale_be.domain.profile.entity.Enum.ProfileType;
import everTale.everTale_be.domain.profile.entity.Profile;
import everTale.everTale_be.domain.profile.repository.ProfileRepository;
import everTale.everTale_be.domain.profile.util.ProfileHelper;
import everTale.everTale_be.domain.story.dto.SceneResponseDTO;
import everTale.everTale_be.domain.story.dto.StoryCollectionResponseDto;
import everTale.everTale_be.domain.story.dto.StoryRequestDTO;
import everTale.everTale_be.domain.story.entity.Scene;
import everTale.everTale_be.domain.story.entity.Story;
import everTale.everTale_be.domain.story.repository.SceneRepository;
import everTale.everTale_be.domain.story.repository.StoryRepository;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.NotFoundHandler;
import everTale.everTale_be.domain.story.external.StoryApiClient;
import everTale.everTale_be.global.apiPayload.exception.handler.UnAuthorizedHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final SceneRepository sceneRepository;
    private final StoryRepository storyRepository;
    private final StoryCharacterRepository storyCharacterRepository;
    private final PersonalityRepository personalityRepository;
    private final ProfileRepository profileRepository;
    private final StoryApiClient storyApiClient;
    private final ProfileHelper profileHelper;

    // Scene 단일 조회
    @Transactional(readOnly = true)
    public SceneResponseDTO getSceneBySceneNum(Long storyId, int sceneNum) {
        Scene scene = findScene(storyId, sceneNum);
        return SceneResponseDTO.from(scene);
    }

    @Transactional
    public String updateSceneContent(Long storyId, int sceneNum, String updatedContent) {
        Scene scene = findScene(storyId, sceneNum);
        scene.updateContent(updatedContent);
        return updatedContent;
    }


    // 스토리 삭제
    @Transactional
    public void deleteStoryWithScenes(Long storyId) {
        Story story = findStory(storyId);
        storyRepository.delete(story);
    }


    // 스토리 생성
    @Transactional
    public Long createEmptyStory() {
        Profile profile = profileHelper.getAuthenticatedProfile();
        Story story = Story.builder()
                        .profile(profile)
                        .build();
        storyRepository.save(story);
        return story.getId();
    }

    //초기 캐릭터 설정
    @Transactional
    public void saveInitialCharacterInfo(Long storyId,
                                         StoryRequestDTO.StoryCharacterInfoRequestDTO request,
                                         MultipartFile initCharacterImage) {
        Story story = findStory(storyId);
        story.updateTitle(request.getTitle());

        // StoryCharacter 생성
        StoryCharacter character = StoryCharacter.builder()
                .name(request.getCharacterName())
                .age(request.getAge())
                .imageUrl(initCharacterImage.getOriginalFilename())
                .gender(Gender.valueOf(request.getGender().toUpperCase()))
                .build();

        // Personality 리스트 저장
        for (String pDesc : request.getPersonalities()) {
            Personality personality = personalityRepository
                    .findByPersonality(pDesc)
                    .orElseGet(() -> personalityRepository.save(Personality.from(pDesc)));

            character.addCharacterPersonality(personality);
        }

        storyCharacterRepository.save(character);

        // 스토리에 캐릭터 연결
        story.setCharacter(character);
    }


    // 초기 줄거리 생성
    @Transactional
    public String generateInitScene(Long storyId, StoryRequestDTO.StoryWorldViewRequestDTO request) {
        // 1. storyId로 스토리 조회
        Story story = findStory(storyId);

        // 2. 연결된 캐릭터 가져오기
        StoryCharacter storyCharacter = story.getCharacter();
        if (storyCharacter == null) {
            throw new NotFoundHandler(ErrorStatus.CHARACTER_NOT_FOUND);
        }

        // 2-1. personalities 문자열 리스트로 변환
        List<String> personalities = storyCharacter.getCharacterPersonalities().stream()
                .map(cp -> cp.getPersonality().getPersonality())
                .collect(Collectors.toList());

        // 3. FastAPI 요청용 JSON 만들기
        StoryRequestDTO.FastApiInitStoryRequestDTO requestDto = StoryRequestDTO.FastApiInitStoryRequestDTO.builder()
                .title(story.getTitle())
                .genre(request.getGenre().name())
                .worldView(request.getWorldView())
                .name(storyCharacter.getName())
                .age(storyCharacter.getAge())
                .gender(storyCharacter.getGender().name())
                .personalities(personalities)
                .build();

        // 4. FastAPI 호출해 초기 줄거리 생성
        String initStory = storyApiClient.callFastApiForInitStory(requestDto);

        // 5. 장르 저장
        story.updateGenre(request.getGenre());


        // 6. 첫 장면 저장 (page=1)
        Scene scene = Scene.builder()
                .page(1)
                .content(initStory)
                .build();

        story.addScene(scene);

        // 7. 줄거리 반환
        return initStory;
    }



    // 이전 장면 기반 다음 줄거리 생성
    @Transactional
    public String generateNextScene(Long storyId, int sceneNum) {
        // 1. 이전 줄거리 조회
        Scene prevScene = findScene(storyId, sceneNum-1);
        String previousContent = prevScene.getContent();

        // 2. Story & Character 조회
        Story story = prevScene.getStory();
        StoryCharacter character = story.getCharacter();

        // 3. Personality 추출
        List<String> personalities = character.getCharacterPersonalities().stream()
                .map(cp -> cp.getPersonality().getPersonality())
                .collect(Collectors.toList());

        // 4. DTO 조립 및 FastAPI 호출
        StoryRequestDTO.NextStoryGenerateRequestDTO dto =
                StoryRequestDTO.NextStoryGenerateRequestDTO.builder()
                        .previous(previousContent)
                        .sceneNum(sceneNum)
                        .genre(story.getGenre().name())
                        .title(story.getTitle())
                        .name(character.getName())
                        .age(character.getAge())
                        .gender(character.getGender().name())
                        .personalities(personalities)
                        .build();

        // 5. FastAPI 호출 → 다음 줄거리 생성
        String nextContent = storyApiClient.callFastApiForNextStory(dto);

        // 6. 새 Scene 저장
        Scene newScene = Scene.builder()
                .page(sceneNum)
                .content(nextContent)
                .build();
        story.addScene(newScene);

        return nextContent;
    }

    // 이전 장면 기반 질문 생성
    @Transactional(readOnly = true)
    public String generateQuestionFromPreviousScene(Long storyId, int sceneNum) {
        Scene prevScene = findScene(storyId, sceneNum - 1);
        return storyApiClient.callFastApiForQuestion(prevScene.getContent());
    }

    // 아이의 대답 기반 다음 줄거리 생성
    @Transactional
    public String generateNextSceneWithAnswer(Long storyId, int sceneNum, String answer) {
        Scene prevScene = findScene(storyId, sceneNum - 1);

        String nextContent = storyApiClient.callFastApiForNextStoryWithAnswer(prevScene.getContent(), answer);

        Story story = prevScene.getStory();

        Scene newScene = Scene.builder()
                .page(sceneNum)
                .content(nextContent)
                .build();
        story.addScene(newScene);
        return nextContent;
    }
    // 줄거리 및 아이그림 기반 그림 생성
    @Transactional
    public String generateImageFromSketch(Long storyId, int sceneNum, MultipartFile sketch) {
        Scene scene = findScene(storyId, sceneNum);

        String prompt = scene.getContent();
        String imageUrl = storyApiClient.callFastApiForImageFromSketch(sketch, prompt, scene.getStory().getGenre().name());

        scene.updateImageUrl(imageUrl);
        return imageUrl;
    }

    // 줄거리 기반 그림 생성
    @Transactional
    public String generateImageFromPrompt(Long storyId, int sceneNum) {
        Scene scene = findScene(storyId, sceneNum);

        String prompt = scene.getContent();
        String imageUrl = storyApiClient.callFastApiForImageFromPrompt(prompt, scene.getStory().getGenre().name());

        scene.updateImageUrl(imageUrl);
        return imageUrl;
    }

    private Scene findScene(Long storyId, int sceneNum){
        Long profileId = profileHelper.getAuthenticatedProfileId();
        return sceneRepository.findByStoryIdAndPageAndStoryProfileId(storyId, sceneNum, profileId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.SCENE_NOT_FOUND));
    }
    private Story findStory(Long storyId) {
        Long profileId = profileHelper.getAuthenticatedProfileId();
        return storyRepository.findByIdAndProfileId(storyId, profileId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.STORY_NOT_FOUND));
    }

    // 모두의 책장
    public StoryCollectionResponseDto getAllStories(Pageable pageable){
        Page<Story> stories = storyRepository.findAll(pageable);
        return StoryCollectionResponseDto.from(stories);
    }

    // 나의 책장
    public StoryCollectionResponseDto getStories(Long profileId, Pageable pageable) {
        Profile profile = profileHelper.getAuthenticatedProfile();

        if (profile.getProfileType()== ProfileType.CHILD) {
            if (!profile.getId().equals(profileId)) {
                throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
            }
        } else {
            boolean isMyChild = profileRepository.existsByUserIdAndId(profile.getId(), profileId);
            if (!isMyChild) {
                throw new UnAuthorizedHandler(ErrorStatus.UNAUTHORIZED_PROFILE_ACCESS);
            }
        }

        Page<Story> stories = storyRepository.findByProfileId(profileId, pageable);
        return StoryCollectionResponseDto.from(stories);
    }

    public String getSceneText(Long storyId, Long sceneId) {
        boolean isStoryExists = storyRepository.existsById(storyId);
        if (!isStoryExists){
            throw new NotFoundHandler(ErrorStatus.STORY_NOT_FOUND);
        }
        Scene scene = sceneRepository.findByIdAndStoryId(sceneId, storyId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.SCENE_NOT_FOUND));

        return scene.getContent();
    }
}
