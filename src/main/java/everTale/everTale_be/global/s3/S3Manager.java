package everTale.everTale_be.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import everTale.everTale_be.global.apiPayload.code.status.ErrorStatus;
import everTale.everTale_be.global.apiPayload.exception.handler.BadRequestHandler;
import everTale.everTale_be.global.entity.Uuid;
import everTale.everTale_be.global.repository.UuidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Manager {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final AmazonS3 amazonS3;
    private final UuidRepository uuidRepository;

    // 지원되는 이미지 확장자
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "mp3", "wav");

    /**
     * 다중 이미지 업로드
     */
    public List<String> uploadMultipleFiles(List<MultipartFile> multipartFiles, String dirName) {
        validateFileList(multipartFiles);

        List<String> uploadedUrls = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            uploadedUrls.add(uploadFile(file, dirName));
        }
        return uploadedUrls;
    }

    /**
     * 단일 이미지 업로드 및 S3 URL 반환
     */
    public String uploadFile(MultipartFile multipartFile, String dirName) {
        Uuid savedUuid = Uuid.createAndSave(uuidRepository);
        String originalFileName = multipartFile.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            throw new BadRequestHandler(ErrorStatus._BAD_REQUEST);
        }

        validateImageExtension(originalFileName);

        String uniqueFileName = savedUuid.getUuid() + "_" + originalFileName.replaceAll("\\s", "_");
        String fileKey = dirName + "/" + uniqueFileName;

        log.info("Uploading file to S3: {}", fileKey);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileKey, inputStream, metadata));
        } catch (IOException e) {
            log.error("S3 upload failed for {}: {}", fileKey, e.getMessage());
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }

        return amazonS3.getUrl(bucket, fileKey).toString();
    }

    /**
     * 이미지 삭제 (S3 + DB UUID)
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            log.warn("fileUrl is null or empty.");
            return;
        }

        try {
            String bucketUrlPrefix = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
            if (!fileUrl.startsWith(bucketUrlPrefix)) {
                log.error("Invalid S3 URL: {}", fileUrl);
                throw new IllegalArgumentException("잘못된 S3 URL입니다.");
            }

            String fileKey = fileUrl.substring(bucketUrlPrefix.length());
            log.info("Extracted file key: {}", fileKey);

            if (!amazonS3.doesObjectExist(bucket, fileKey)) {
                log.warn("File not found in S3: {}", fileKey);
                return;
            }

            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileKey));
            log.info("Deleted file from S3: {}", fileKey);

            String uuid = extractUuidFromFileKey(fileKey);
            uuidRepository.deleteByUuid(uuid);
            log.info("Deleted associated UUID: {}", uuid);

        } catch (Exception e) {
            log.error("S3 delete error: {}", e.getMessage());
            throw new RuntimeException("파일 삭제에 실패했습니다.", e);
        }
    }

    /**
     * 파일 키에서 UUID 추출
     */
    private String extractUuidFromFileKey(String fileKey) {
        int underscoreIndex = fileKey.indexOf("_");
        if (underscoreIndex == -1) {
            throw new IllegalArgumentException("파일명 형식이 잘못되었습니다. UUID를 추출할 수 없습니다.");
        }
        return fileKey.substring(fileKey.lastIndexOf("/") + 1, underscoreIndex);
    }

    /**
     * 이미지 확장자 유효성 검사
     */
    public void validateImageExtension(String fileName) {
        int lastDot = fileName.lastIndexOf(".");
        if (lastDot == -1) {
            throw new BadRequestHandler(ErrorStatus.NO_FILE_EXTENTION);
        }

        String extension = fileName.substring(lastDot + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestHandler(ErrorStatus.PICTURE_EXTENSION_ERROR);
        }
    }

    /**
     * 파일 리스트 유효성 검사
     */
    private void validateFileList(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new BadRequestHandler(ErrorStatus._BAD_REQUEST);
        }
    }
}
