package com.viglet.social.rtc.exception;

public enum VigRTCExceptions {
    MEMBER_NOT_FOUND("0001"), //
    INVALID_RECIPIENT("0002"), //
    MEMBER_IN_OTHER_CONVERSATION("0003"),

    INVALID_CONVERSATION_NAME("0101"), //
    CONVERSATION_NAME_OCCUPIED("0102"), //
    CONVERSATION_NOT_FOUND("0103"), //

    UNKNOWN_ERROR("0501"),;

    private String code;

    VigRTCExceptions(String code) {
        this.code = code;
    }

    public String getErrorCode() {
        return code;
    }

    public VigRTCSignalingException exception() {
        return new VigRTCSignalingException(this);
    }

    public VigRTCSignalingException exception(String customMesage) {
        return new VigRTCSignalingException(this, customMesage);
    }

    public VigRTCSignalingException exception(Exception reason) {
        return new VigRTCSignalingException(this, reason);
    }

}
