package everTale.everTale_be.global.apiPayload.exception.handler;


import everTale.everTale_be.global.apiPayload.code.BaseErrorCode;
import everTale.everTale_be.global.apiPayload.exception.GeneralException;

public class NotificationHandler extends GeneralException {
    public NotificationHandler(BaseErrorCode errorCode){super((errorCode));}
}
