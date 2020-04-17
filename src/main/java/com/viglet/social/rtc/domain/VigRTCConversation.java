package com.viglet.social.rtc.domain;

import com.viglet.social.rtc.api.dto.VigRTCConversationDTO;
import com.viglet.social.rtc.repository.VigRTCConversations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public abstract class VigRTCConversation implements VigRTCConversationDTO {

    protected final String id;

    @Autowired
    private VigRTCConversations conversations;

    public VigRTCConversation(String id) {
        this.id = id;
    }

    public abstract void join(VigRTCMember sender);

    public void left(VigRTCMember sender) {
        if (remove(sender)) {
            if (isWithoutMember()) {
                unregisterConversation(sender, this);
            }
        }
    }

    protected abstract boolean remove(VigRTCMember leaving);

    protected void assignSenderToConversation(VigRTCMember sender) {
        sender.assign(this);
    }

    public abstract boolean isWithoutMember();

    public abstract boolean has(VigRTCMember from);

    private void unregisterConversation(VigRTCMember sender, VigRTCConversation conversation) {
        conversations.remove(conversation.getId(), sender);
    }

    public String getId() {
        return this.id;
    }

    public abstract void exchangeSignals(VigRTCInternalMessage message);

    protected void sendJoinedToConversation(VigRTCMember sender, String id) {
        VigRTCInternalMessage.create()//
                .to(sender)//
                .content(id)//
                .signal(VigRTCSignal.JOINED)//
                .build()//
                .send();
    }

    protected void sendJoinedFrom(VigRTCMember sender, VigRTCMember member) {
        VigRTCInternalMessage.create()//
                .from(sender)//
                .to(member)//
                .signal(VigRTCSignal.NEW_JOINED)//
                .content(sender.getId())
                .build()//
                .send();
    }

    protected void sendLeftMessage(VigRTCMember leaving, VigRTCMember recipient) {
        VigRTCInternalMessage.create()//
                .from(leaving)//
                .to(recipient)//
                .signal(VigRTCSignal.LEFT)//
                .build()//
                .send();
    }

    public abstract void broadcast(VigRTCMember from, VigRTCInternalMessage message);

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + id;
    }
}
