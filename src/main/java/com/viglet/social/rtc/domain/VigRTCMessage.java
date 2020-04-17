package com.viglet.social.rtc.domain;

import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class VigRTCMessage {
    /**
     * Use VigRTCMessage.create(...) instead of new VigRTCMessage()
     */
    @Deprecated
    VigRTCMessage() {
    }

    @Expose
    private String from = EMPTY;

    @Expose
    private String to = EMPTY;

    @Expose
    private String signal = EMPTY;

    @Expose
    private String content = EMPTY;

    @Expose
    private Map<String, String> custom = Maps.newHashMap();

    @Override
    public String toString() {
        return String.format("(%s -> %s)[%s]: %s |%s", from, to, signal, content, custom);
    }

    public static VigRTCMessageBuilder create() {
        return new VigRTCMessageBuilder();
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public String getSignal() {
        return this.signal;
    }

    public String getContent() {
        return this.content;
    }

    public Map<String, String> getCustom() {
        return this.custom;
    }

    public static class VigRTCMessageBuilder {
        private VigRTCMessage instance = new VigRTCMessage();

        public VigRTCMessageBuilder from(String from) {
            instance.from = from;
            return this;
        }

        public VigRTCMessageBuilder to(String to) {
            instance.to = to;
            return this;
        }

        public VigRTCMessageBuilder signal(String signal) {
            instance.signal = signal;
            return this;
        }

        public VigRTCMessageBuilder content(String content) {
            instance.content = content;
            return this;
        }

        public VigRTCMessageBuilder custom(Map<String, String> custom) {
            instance.custom.putAll(custom);
            return this;
        }

        public VigRTCMessage build() {
            return instance;
        }

    }
}
