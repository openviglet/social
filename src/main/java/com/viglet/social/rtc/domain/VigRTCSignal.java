package com.viglet.social.rtc.domain;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class VigRTCSignal {
    public static final VigRTCSignal EMPTY = new VigRTCSignal(VigRTCSignals.EMPTY);
    public static final VigRTCSignal OFFER_REQUEST = new VigRTCSignal(VigRTCSignals.OFFER_REQUEST);
    public static final VigRTCSignal OFFER_RESPONSE = new VigRTCSignal(VigRTCSignals.OFFER_RESPONSE, VigRTCSignals.OFFER_RESPONSE_HANDLER);
    public static final VigRTCSignal ANSWER_REQUEST = new VigRTCSignal(VigRTCSignals.ANSWER_REQUEST);
    public static final VigRTCSignal ANSWER_RESPONSE = new VigRTCSignal(VigRTCSignals.ANSWER_RESPONSE, VigRTCSignals.ANSWER_RESPONSE_HANDLER);
    public static final VigRTCSignal FINALIZE = new VigRTCSignal(VigRTCSignals.FINALIZE);
    public static final VigRTCSignal CANDIDATE = new VigRTCSignal(VigRTCSignals.CANDIDATE, VigRTCSignals.CANDIDATE_HANDLER);
    public static final VigRTCSignal PING = new VigRTCSignal(VigRTCSignals.PING);
    public static final VigRTCSignal LEFT = new VigRTCSignal(VigRTCSignals.LEFT, VigRTCSignals.LEFT_HANDLER);
    public static final VigRTCSignal JOIN = new VigRTCSignal(VigRTCSignals.JOIN, VigRTCSignals.JOIN_HANDLER);
    public static final VigRTCSignal CREATE = new VigRTCSignal(VigRTCSignals.CREATE, VigRTCSignals.CREATE_HANDLER);
    public static final VigRTCSignal JOINED = new VigRTCSignal(VigRTCSignals.JOINED);
    public static final VigRTCSignal NEW_JOINED = new VigRTCSignal(VigRTCSignals.NEW_JOINED);
    public static final VigRTCSignal CREATED = new VigRTCSignal(VigRTCSignals.CREATED);
    public static final VigRTCSignal TEXT = new VigRTCSignal(VigRTCSignals.TEXT, VigRTCSignals.TEXT_HANDLER);
    public static final VigRTCSignal ERROR = new VigRTCSignal(VigRTCSignals.ERROR);
    public static final VigRTCSignal END = new VigRTCSignal(VigRTCSignals.END);

    private static final VigRTCSignal[] signals = new VigRTCSignal[]{EMPTY, OFFER_REQUEST,
            OFFER_RESPONSE, ANSWER_REQUEST, ANSWER_RESPONSE, FINALIZE, CANDIDATE,
            PING, LEFT, JOIN, CREATE, JOINED, NEW_JOINED, CREATED, TEXT, ERROR, END
    };
    private final String signalName;
    private final String signalHandler;

    VigRTCSignal(String signalName) {
        this(signalName, VigRTCSignals.EMPTY_HANDLER);
    }

    VigRTCSignal(String signalName, String signalHandler) {
        this.signalName = signalName;
        this.signalHandler = signalHandler;
    }

    public boolean is(String string) {
        return ordinaryName().equalsIgnoreCase(string);
    }

    public boolean is(VigRTCSignal signal) {
        return this.equals(signal);
    }

    public String ordinaryName() {
        return signalName;
    }

    public String handlerName() {
        return signalHandler;
    }

    public static VigRTCSignal fromString(String string) {
        String signalName = defaultString(string);
        for (VigRTCSignal existing : signals) {
            if (existing.signalName.equalsIgnoreCase(signalName)) {
                return existing;
            }
        }
        return new VigRTCSignal(signalName);
    }

    public static VigRTCSignal[] values() {
        return signals;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VigRTCSignal)) {
            return false;
        }
        VigRTCSignal that = (VigRTCSignal) obj;
        return signalName.equalsIgnoreCase(that.signalName);
    }

    @Override
    public int hashCode() {
        return signalName.hashCode();
    }

}
