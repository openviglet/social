package com.viglet.social.rtc.api;

import com.viglet.social.rtc.api.dto.VigRTCEvent;
import com.viglet.social.rtc.api.dto.VigRTCMemberDTO;
import com.viglet.social.rtc.domain.VigRTCConversation;
import com.viglet.social.rtc.domain.VigRTCEventContext;
import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.exception.VigRTCExceptions;

import javax.websocket.Session;

public enum VigRTCEvents {
    SESSION_OPENED,
    SESSION_CLOSED,
    CONVERSATION_CREATED,
    CONVERSATION_DESTROYED,
    UNEXPECTED_SITUATION,
    MEMBER_JOINED,
    MEMBER_LEFT,
    MEDIA_LOCAL_STREAM_REQUESTED,
    MEDIA_LOCAL_STREAM_CREATED,
    MEDIA_STREAMING,
    TEXT;

    public VigRTCEvent basedOn(VigRTCInternalMessage message, VigRTCConversation conversation) {
        return VigRTCEventContext.builder()
                .from(message.getFrom())
                .to(message.getTo())
                .custom(message.getCustom())
                .conversation(conversation)
                .type(this)
                .build();
    }

    public VigRTCEvent basedOn(VigRTCEventContext.VigRTCEventContextBuilder builder) {
        return builder
                .type(this)
                .build();
    }

    public VigRTCEvent occurFor(Session session, String reason) {
        return VigRTCEventContext.builder()
                .from(new VigRTCInternalMember(session))
                .type(this)
                .reason(reason)
                .build();
    }

    public VigRTCEvent occurFor(Session session) {
        return VigRTCEventContext.builder()
                .type(this)
                .from(new VigRTCInternalMember(session))
                .exception(VigRTCExceptions.UNKNOWN_ERROR.exception())
                .build();
    }

    public VigRTCEvent basedOn(VigRTCInternalMessage message) {
        return VigRTCEventContext.builder()
                .from(message.getFrom())
                .to(message.getTo())
                .custom(message.getCustom())
                .content(message.getContent())
                .type(this)
                .build();
    }

    private static class VigRTCInternalMember implements VigRTCMemberDTO {

        private final Session session;

        VigRTCInternalMember(Session session) {
            this.session = session;
        }

        @Override
        public Session getSession() {
            return session;
        }

        @Override
        public String getId() {
            if (session == null) {
                return null;
            }
            return session.getId();
        }

        @Override
        public String toString() {
            return getId();
        }
    }
}
