package com.viglet.social.rtc.cases;

import com.viglet.social.rtc.VigRTCNames;
import com.viglet.social.rtc.api.VigRTCEventBus;
import com.viglet.social.rtc.domain.VigRTCConversation;
import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.domain.VigRTCMember;
import com.viglet.social.rtc.domain.VigRTCSignals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.viglet.social.rtc.api.VigRTCEvents.TEXT;

@Component(VigRTCSignals.TEXT_HANDLER)
public class VigRTCTextMessage implements VigRTCSignalHandler {

    @Autowired
    @Qualifier(VigRTCNames.EVENT_BUS)
    private VigRTCEventBus eventBus;

    @Override
    public void execute(VigRTCInternalMessage message) {
        VigRTCMember from = message.getFrom();
        if (message.getTo() == null && from.getConversation().isPresent()) {
            VigRTCConversation conversation = from.getConversation().get();
            conversation.broadcast(from, message);
            eventBus.post(TEXT.basedOn(message));
        } else if (from.hasSameConversation(message.getTo())) {
            message.send();
            eventBus.post(TEXT.basedOn(message));
        }

    }
}
