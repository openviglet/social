package com.viglet.social.rtc.cases;

import com.viglet.social.rtc.domain.VigRTCConversation;
import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.domain.VigRTCMember;
import com.viglet.social.rtc.domain.VigRTCSignals;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.viglet.social.rtc.exception.VigRTCExceptions.CONVERSATION_NOT_FOUND;

@Component(VigRTCSignals.LEFT_HANDLER)
public class VigRTCLeftConversation implements VigRTCSignalHandler {

    public void execute(VigRTCInternalMessage context) {
        final VigRTCMember leaving = context.getFrom();
        VigRTCConversation conversation = checkPrecondition(leaving.getConversation());

        conversation.left(leaving);
    }

    private VigRTCConversation checkPrecondition(Optional<VigRTCConversation> conversation) {
        if (!conversation.isPresent()) {
            throw CONVERSATION_NOT_FOUND.exception();
        }
        return conversation.get();
    }

}
