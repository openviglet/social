package com.viglet.social.rtc.exception;

import org.apache.commons.lang3.StringUtils;

import static java.lang.String.format;

public class VigRTCSignalingException extends RuntimeException {

    private static final long serialVersionUID = 4171073365651049929L;

    private String errorCode;
    private String customMessage;

    public VigRTCSignalingException(VigRTCExceptions exception) {
        super(exception.getErrorCode() + ": " + exception.name());
        this.errorCode = exception.getErrorCode();
    }

    public VigRTCSignalingException(VigRTCExceptions exception, Throwable t) {
        super(exception.getErrorCode() + ": " + exception.name(), t);
        this.errorCode = exception.getErrorCode();
    }

    public VigRTCSignalingException(VigRTCExceptions exception, String customMessage) {
        super(exception.getErrorCode() + ": " + exception.name());
        this.errorCode = exception.getErrorCode();
        this.customMessage = customMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getCustomMessage() {
        return StringUtils.defaultString(customMessage);
    }

    public void throwException() {
        throw this;
    }

    @Override
    public String toString() {
        return format("Signaling Exception (CODE: %s) %s [%s]", getErrorCode(), getMessage(), getCustomMessage());
    }

}
