package com.viglet.social.rtc.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.viglet.social.rtc.VigRTCNames;
import com.viglet.social.rtc.api.VigRTCEventBus;
import com.viglet.social.rtc.api.dto.VigRTCMemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

import static com.viglet.social.rtc.api.VigRTCEvents.MEMBER_JOINED;
import static com.viglet.social.rtc.api.VigRTCEvents.MEMBER_LEFT;
import static com.viglet.social.rtc.domain.VigRTCEventContext.builder;

@Component
@Scope("prototype")
public class VigRTCMember implements VigRTCMemberDTO {

    private String id;
    private Session session;
    private VigRTCConversation conversation;

    @Autowired
    @Qualifier(VigRTCNames.EVENT_BUS)
    private VigRTCEventBus eventBus;

    private ScheduledFuture<?> ping;

    public VigRTCMember(Session session, ScheduledFuture<?> ping) {
        this.id = session.getId();
        this.session = session;
        this.ping = ping;
    }

    public Optional<VigRTCConversation> getConversation() {
        return Optional.ofNullable(conversation);
    }

    public void markLeft() {
        ping.cancel(true);
    }

    public void assign(VigRTCConversation conversation) {
        this.conversation = conversation;
        eventBus.post(MEMBER_JOINED.basedOn(
                builder()
                        .conversation(conversation)
                        .from(this)));
    }

    public void unassignConversation(VigRTCConversation conversation) {
        eventBus.post(MEMBER_LEFT.basedOn(
                builder()
                        .conversation(conversation)
                        .from(this)));
        this.conversation = null;
    }

    public String getId() {
        return this.id;
    }

    public Session getSession() {
        return this.session;
    }

    public boolean hasSameConversation(VigRTCMember to) {
        if (to == null) {
            return false;
        }
        return conversation.equals(to.conversation);
    }

    @Override
    public String toString() {
        return String.format("%s", id);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VigRTCMember)) {
            return false;
        }
        VigRTCMember m = (VigRTCMember) o;
        return new EqualsBuilder()//
                .append(m.id, id)//
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()//
                .append(id)//
                .build();
    }
}
