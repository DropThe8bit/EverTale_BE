package everTale.everTale_be.global.apiPayload.code.status;

import everTale.everTale_be.global.apiPayload.code.BaseErrorCode;
import everTale.everTale_be.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    //일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // Not Found
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER404", "해당 유저를 찾을 수 없습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "TOKEN404", "리프레시 토큰이 존재하지 않습니다."),

    // User 관련 에러
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER4002", "닉네임은 필수입니다."),
    ALREADY_EXISTS_EMAIL(HttpStatus.CONFLICT, "USER409", "이미 존재하는 이메일입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "USER401", "이메일 또는 비밀번호가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "USER4012", "유효하지 않은 리프레시 토큰입니다."),
    BLOCKED_TOKEN(HttpStatus.FORBIDDEN, "USER403", "블랙리스트에 있는 토큰입니다. 다시 로그인 해주세요."),

    // Profile 관련 에러
    ALREADY_EXISTS_PROFILE(HttpStatus.CONFLICT, "PROFILE409", "중복되는 프로필 이름입니다."),
    NOT_FOUND_PROFILE(HttpStatus.NOT_FOUND, "PROFILE404", "해당 프로필을 찾을 수 없습니다."),
    UNAUTHORIZED_PROFILE_ACCESS(HttpStatus.UNAUTHORIZED, "PROFILE401", "해당 프로필에 대한 접근이 거부되었습니다."),
    ALREADY_EXISTS_PARENT_PROFILE(HttpStatus.CONFLICT, "PROFILE4092", "부모 프로필이 이미 존재합니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "PROFILE4012", "비밀번호가 일치하지 않습니다."),

    //s3 관련 에러
    NO_FILE_EXTENTION(HttpStatus.BAD_REQUEST, "UPLOAD400", "파일의 이름에 확장자가 존재하지 않습니다."),
    PICTURE_EXTENSION_ERROR(HttpStatus.BAD_REQUEST, "PICTURE400", "이미지의 확장자가 잘못되었습니다."),
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "UPLOAD413", "파일 크기가 허용 범위를 초과했습니다."),

    // Story 관련 에러
    STORY_NOT_FOUND(HttpStatus.NOT_FOUND, "STORY404", "스토리를 찾을 수 없습니다."),
    SCENE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCENE404", "이전 장면을 찾을 수 없습니다."),
    CHARACTER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHARACTER404", "스토리에 연결된 캐릭터가 존재하지 않습니다."),
    INVALID_SCENE_NUMBER(HttpStatus.BAD_REQUEST, "SCENE400", "유효하지 않은 장면 번호입니다."),
    FASTAPI_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FASTAPI500", "FastAPI 서버 호출 중 오류가 발생했습니다."),
    FASTAPI_IMAGE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FASTAPI501", "이미지 저장 중 오류가 발생했습니다."),
    ENABLE_TO_GENERATE_IMAGE(HttpStatus.BAD_REQUEST,"FASTAPI502","이미지 생성 중 오류가 발생했습니다."),
    ENABLE_TO_GENERATE_STORY(HttpStatus.BAD_REQUEST,"FASTPAI503","줄거리 생성 중 오류가 발생했습니다."),
    ENABLE_TO_GENERATE_QUESTION(HttpStatus.BAD_REQUEST,"FASTPAI504","질문 생성 중 오류가 발생했습니다."),


    // EasterEgg 관련
    EASTER_EGG_LETTER_NOT_FOUND(HttpStatus.NOT_FOUND,"EASTER_EGG400","이스터에그 편지를 찾을 수 없습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
