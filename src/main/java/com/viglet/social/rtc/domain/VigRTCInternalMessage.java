package com.viglet.social.rtc.domain;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import javax.websocket.RemoteEndpoint.Async;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultString;

public class VigRTCInternalMessage {

    private static final Logger log = Logger.getLogger(VigRTCInternalMessage.class);
    private VigRTCMember from;
    private VigRTCMember to;
    private VigRTCSignal signal;
    private String content;
    private Map<String, String> custom = Maps.newHashMap();

    private VigRTCInternalMessage(VigRTCMember from, VigRTCMember to, VigRTCSignal signal, String content, Map<String, String> custom) {
        this.from = from;
        this.to = to;
        this.signal = signal;
        this.content = content;
        if (custom != null) {
            this.custom.putAll(custom);
        }
    }

    public static VigRTCInternalMessageBuilder create() {
        return new VigRTCInternalMessageBuilder();
    }

    public VigRTCInternalMessageBuilder copy() {
        return new VigRTCInternalMessageBuilder().from(from).to(to).content(content).custom(custom).signal(signal);
    }

    /**
     * Method will send message to recipient (member To)
     */
    public void send() {
        if (signal != VigRTCSignal.PING) {
            log.info("Outgoing: " + toString());
        }
        getRemotePeer().sendObject(transformToExternalMessage());
    }

    public void sendCarefully() {
        if (to.getSession().isOpen()) {
            getRemotePeer().sendObject(transformToExternalMessage());
        } else {
            log.debug("Unable to send message: " + transformToExternalMessage() + " session is broken!");
        }
    }

    private VigRTCMessage transformToExternalMessage() {
        return VigRTCMessage.create()//
                .from(fromNullable(from))//
                .to(fromNullable(to))//
                .signal(signal.ordinaryName())//
                .content(defaultString(content))//
                .custom(custom)//
                .build();
    }

    private String fromNullable(VigRTCMember member) {
        return member == null ? EMPTY : member.getId();
    }

    private Async getRemotePeer() {
        return to.getSession().getAsyncRemote();
    }

    @Override
    public String toString() {
        return String.format("(%s -> %s)[%s]: %s |%s", from, to, signal != null ? signal.ordinaryName() : null, content, custom);
    }

    public VigRTCMember getFrom() {
        return this.from;
    }

    public VigRTCMember getTo() {
        return this.to;
    }

    public VigRTCSignal getSignal() {
        return this.signal;
    }

    public String getContent() {
        return this.content;
    }

    public Map<String, String> getCustom() {
        return this.custom;
    }

    public static class VigRTCInternalMessageBuilder {
        private VigRTCMember from;
        private VigRTCMember to;
        private VigRTCSignal signal;
        private String content;
        private Map<String, String> custom = Maps.newHashMap();

        VigRTCInternalMessageBuilder() {
        }

        public VigRTCInternalMessage.VigRTCInternalMessageBuilder from(VigRTCMember from) {
            this.from = from;
            return this;
        }

        public VigRTCInternalMessage.VigRTCInternalMessageBuilder to(VigRTCMember to) {
            this.to = to;
            return this;
        }

        public VigRTCInternalMessage.VigRTCInternalMessageBuilder signal(VigRTCSignal signal) {
            this.signal = signal;
            return this;
        }

        public VigRTCInternalMessage.VigRTCInternalMessageBuilder content(String content) {
            this.content = content;
            return this;
        }

        public VigRTCInternalMessage.VigRTCInternalMessageBuilder custom(Map<String, String> custom) {
            if (custom != null) {
                this.custom.putAll(custom);
            }
            return this;
        }

        public VigRTCInternalMessage.VigRTCInternalMessageBuilder addCustom(String key, String value) {
            this.custom.put(key, value);
            return this;
        }

        public VigRTCInternalMessage build() {
            return new VigRTCInternalMessage(from, to, signal, content, custom);
        }

        public String toString() {
            return "com.viglet.social.rtc.domain.InternalMessage.InternalMessageBuilder(from=" + this.from + ", to=" + this.to + ", signal=" + this.signal + ", content=" + this.content + ", custom=" + this.custom + ")";
        }
    }
}
