package com.viglet.social.rtc.domain.conversation;

import com.google.common.collect.Sets;
import com.viglet.social.rtc.VigRTCNames;
import com.viglet.social.rtc.api.VigRTCEventBus;
import com.viglet.social.rtc.cases.VigRTCExchangeSignalsBetweenMembers;
import com.viglet.social.rtc.domain.VigRTCConversation;
import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.domain.VigRTCMember;
import com.viglet.social.rtc.domain.VigRTCSignal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Scope("prototype")
public class VigRTCMeshConversation extends VigRTCConversation {
    @Autowired
    private VigRTCExchangeSignalsBetweenMembers exchange;

    @Autowired
    @Qualifier(VigRTCNames.EVENT_BUS)
    private VigRTCEventBus eventBus;

    private Set<VigRTCMember> members = Sets.newConcurrentHashSet();

    public VigRTCMeshConversation(String id) {
        super(id);
    }

    @Override
    public synchronized void join(VigRTCMember sender) {
        assignSenderToConversation(sender);

        informSenderThatHasBeenJoined(sender);

        informRestAndBeginSignalExchange(sender);

        members.add(sender);
    }

    private void informRestAndBeginSignalExchange(VigRTCMember sender) {
        for (VigRTCMember to : members) {
            sendJoinedFrom(sender, to);
            exchange.begin(to, sender);
        }
    }

    private void informSenderThatHasBeenJoined(VigRTCMember sender) {
        if (isWithoutMember()) {
            sendJoinedToFirst(sender, id);
        } else {
            sendJoinedToConversation(sender, id);
        }
    }

    public synchronized boolean isWithoutMember() {
        return members.isEmpty();
    }

    public synchronized boolean has(VigRTCMember member) {
        return member != null && members.contains(member);
    }

    @Override
    public void exchangeSignals(VigRTCInternalMessage message) {
        exchange.execute(message);
    }

    @Override
    public void broadcast(VigRTCMember from, VigRTCInternalMessage message) {
        members.stream()
                .filter(member -> !member.equals(from))
                .forEach(to -> message.copy()
                        .from(from)
                        .to(to)
                        .build()
                        .send());
    }

    @Override
    public synchronized boolean remove(VigRTCMember leaving) {
        boolean remove = members.remove(leaving);
        if (remove) {
            leaving.unassignConversation(this);
            for (VigRTCMember member : members) {
                sendLeftMessage(leaving, member);
            }
        }
        return remove;
    }

    private void sendJoinedToFirst(VigRTCMember sender, String id) {
        VigRTCInternalMessage.create()//
                .to(sender)//
                .signal(VigRTCSignal.CREATED)//
                .addCustom("type", "MESH")
                .content(id)//
                .build()//
                .send();
    }

}
