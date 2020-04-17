package com.viglet.social.rtc.cases;

import com.viglet.social.rtc.domain.VigRTCConversation;
import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.domain.VigRTCMember;

import static com.viglet.social.rtc.exception.VigRTCExceptions.INVALID_RECIPIENT;

public abstract class VigRTCExchange implements VigRTCSignalHandler {

    @Override
    public final void execute(VigRTCInternalMessage message) {
        VigRTCConversation conversation = checkPrecondition(message.getFrom());
        exchange(message, conversation);
    }

    protected abstract void exchange(VigRTCInternalMessage message, VigRTCConversation conversation);

    private VigRTCConversation checkPrecondition(VigRTCMember from) {
        if (!from.getConversation().isPresent()) {
            throw INVALID_RECIPIENT.exception();
        }
        return from.getConversation().get();
    }
}
