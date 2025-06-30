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

    //s3 관련 에러
    NO_FILE_EXTENTION(HttpStatus.BAD_REQUEST, "UPLOAD400", "파일의 이름에 확장자가 존재하지 않습니다."),
    PICTURE_EXTENSION_ERROR(HttpStatus.BAD_REQUEST, "PICTURE400", "이미지의 확장자가 잘못되었습니다."),
    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "UPLOAD413", "파일 크기가 허용 범위를 초과했습니다."),
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
