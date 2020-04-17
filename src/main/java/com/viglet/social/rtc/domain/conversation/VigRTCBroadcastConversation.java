package com.viglet.social.rtc.domain.conversation;

import com.google.common.collect.Sets;
import com.viglet.social.rtc.cases.VigRTCExchangeSignalsBetweenMembers;
import com.viglet.social.rtc.domain.VigRTCConversation;
import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.domain.VigRTCMember;
import com.viglet.social.rtc.domain.VigRTCSignal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Scope("prototype")
public class VigRTCBroadcastConversation extends VigRTCConversation {
    @Autowired
    private VigRTCExchangeSignalsBetweenMembers exchange;

    private VigRTCMember broadcaster;
    private Set<VigRTCMember> audience = Sets.newConcurrentHashSet();

    public VigRTCBroadcastConversation(String id) {
        super(id);
    }

    @Override
    public synchronized void join(VigRTCMember sender) {
        assignSenderToConversation(sender);

        informSenderThatHasBeenJoined(sender);

        beginSignalExchangeBetweenBroadcasterAndNewAudience(sender);

        if (!broadcaster.equals(sender)) {
            audience.add(sender);
        }
    }

    @Override
    public synchronized boolean remove(VigRTCMember leaving) {
        if (broadcaster.equals(leaving)) {
            for (VigRTCMember member : audience) {
                sendLeftMessage(broadcaster, member);
                sendEndMessage(broadcaster, member);
                member.unassignConversation(this);
            }
            audience.clear();
            broadcaster.unassignConversation(this);
            broadcaster = null;
            return true;
        }
        sendLeftMessage(leaving, broadcaster);
        boolean remove = audience.remove(leaving);
        if (remove) {
            leaving.unassignConversation(this);
        }
        return remove;
    }

    private void sendEndMessage(VigRTCMember leaving, VigRTCMember recipient) {
        VigRTCInternalMessage.create()//
                .from(leaving)//
                .to(recipient)//
                .signal(VigRTCSignal.END)//
                .content(id)//
                .build()//
                .send();
    }

    @Override
    public synchronized boolean isWithoutMember() {
        if (broadcaster != null) {
            return false;
        }
        return audience.isEmpty();
    }

    @Override
    public synchronized boolean has(VigRTCMember from) {
        if (broadcaster == null) {
            return false;
        }
        if (broadcaster.equals(from)) {
            return true;
        }
        return audience.contains(from);
    }

    @Override
    public void exchangeSignals(VigRTCInternalMessage message) {
        exchange.execute(message);
    }

    @Override
    public void broadcast(VigRTCMember from, VigRTCInternalMessage message) {
        audience.stream()
                .filter(member -> !member.equals(from))
                .forEach(to -> message.copy()
                        .from(from)
                        .to(to)
                        .build()
                        .send());
        if (from != broadcaster) {
            message.copy()
                    .from(from)
                    .to(broadcaster)
                    .build()
                    .send();
        }
    }

    private void informSenderThatHasBeenJoined(VigRTCMember sender) {
        if (isWithoutMember()) {
            broadcaster = sender;
            sendJoinedToBroadcaster(sender, id);
        } else {
            sendJoinedToConversation(sender, id);
        }
    }

    private void beginSignalExchangeBetweenBroadcasterAndNewAudience(VigRTCMember sender) {
        if (!sender.equals(broadcaster)) {
            sendJoinedFrom(sender, broadcaster);
            exchange.begin(broadcaster, sender);
        }
    }

    private void sendJoinedToBroadcaster(VigRTCMember sender, String id) {
        VigRTCInternalMessage.create()//
                .to(sender)//
                .signal(VigRTCSignal.CREATED)//
                .addCustom("type", "BROADCAST")
                .content(id)//
                .build()//
                .send();
    }

}
