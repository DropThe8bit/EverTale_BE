package everTale.everTale_be.global.apiPayload.exception.handler;


import everTale.everTale_be.global.apiPayload.code.BaseErrorCode;
import everTale.everTale_be.global.apiPayload.exception.GeneralException;

public class UnAuthorizedHandler extends GeneralException {
    public UnAuthorizedHandler(BaseErrorCode baseErrorCode) {super(baseErrorCode);}
}
