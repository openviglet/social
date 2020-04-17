package com.viglet.social.rtc.domain;

import com.google.common.collect.Maps;
import org.joda.time.DateTime;
import com.viglet.social.rtc.api.VigRTCEvents;
import com.viglet.social.rtc.api.dto.VigRTCConversationDTO;
import com.viglet.social.rtc.api.dto.VigRTCEvent;
import com.viglet.social.rtc.api.dto.VigRTCMemberDTO;
import com.viglet.social.rtc.exception.VigRTCSignalingException;

import java.util.Map;
import java.util.Optional;

import static java.util.Collections.unmodifiableMap;
import static java.util.Optional.ofNullable;

public class VigRTCEventContext implements VigRTCEvent {

    private final VigRTCEvents type;
    private final DateTime published = DateTime.now();
    private final Map<String, String> custom = Maps.newHashMap();
    private final Optional<VigRTCMemberDTO> from;
    private final Optional<VigRTCMemberDTO> to;
    private final Optional<VigRTCConversationDTO> conversation;
    private final Optional<VigRTCSignalingException> exception;
    private final String reason;
    private final String content;

    private VigRTCEventContext(VigRTCEvents type, VigRTCMemberDTO from, VigRTCMemberDTO to, VigRTCConversation conversation, VigRTCSignalingException exception, String reason, String content) {
        this.type = type;
        this.from = ofNullable(from);
        this.to = ofNullable(to);
        this.conversation = ofNullable(conversation);
        this.exception = ofNullable(exception);
        this.reason = reason;
        this.content = content;
    }

    @Override
    public VigRTCEvents type() {
        return type;
    }

    @Override
    public DateTime published() {
        return published;
    }

    @Override
    public Optional<VigRTCMemberDTO> from() {
        return from;
    }

    @Override
    public Optional<VigRTCMemberDTO> to() {
        return to;
    }

    @Override
    public Optional<VigRTCConversationDTO> conversation() {
        return conversation;
    }

    @Override
    public Optional<VigRTCSignalingException> exception() {
        return exception;
    }

    @Override
    public Map<String, String> custom() {
        return unmodifiableMap(custom);
    }

    @Override
    public Optional<String> reason() {
        return Optional.ofNullable(reason);
    }

    @Override
    public Optional<String> content() {
        return Optional.ofNullable(content);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) <- %s -> (%s)", type, from, conversation, to);
    }

    public static VigRTCEventContextBuilder builder() {
        return new VigRTCEventContextBuilder();
    }

    public static class VigRTCEventContextBuilder {
        private Map<String, String> custom;
        private VigRTCEvents type;
        private VigRTCMemberDTO from;
        private VigRTCMemberDTO to;
        private VigRTCConversation conversation;
        private VigRTCSignalingException exception;
        private String reason;
        private String content;

        public VigRTCEventContextBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public VigRTCEventContextBuilder content(String content) {
            this.content = content;
            return this;
        }

        public VigRTCEventContextBuilder type(VigRTCEvents type) {
            this.type = type;
            return this;
        }

        public VigRTCEventContextBuilder custom(Map<String, String> custom) {
            this.custom = custom;
            return this;
        }

        public VigRTCEventContextBuilder from(VigRTCMemberDTO from) {
            this.from = from;
            return this;
        }

        public VigRTCEventContextBuilder to(VigRTCMember to) {
            this.to = to;
            return this;
        }

        public VigRTCEventContextBuilder conversation(VigRTCConversation conversation) {
            this.conversation = conversation;
            return this;
        }

        public VigRTCEventContextBuilder exception(VigRTCSignalingException exception) {
            this.exception = exception;
            return this;
        }

        public VigRTCEvent build() {
            if (type == null) {
                throw new IllegalArgumentException("Type is required");
            }
            VigRTCEventContext eventContext = new VigRTCEventContext(type, from, to, conversation, exception, reason, content);
            if (custom != null) {
                eventContext.custom.putAll(custom);
            }
            return eventContext;
        }
    }
}
