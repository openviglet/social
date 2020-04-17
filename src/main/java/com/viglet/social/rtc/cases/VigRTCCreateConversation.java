package com.viglet.social.rtc.cases;

import com.viglet.social.rtc.domain.VigRTCConversation;
import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.domain.VigRTCSignals;
import com.viglet.social.rtc.exception.VigRTCSignalingException;
import com.viglet.social.rtc.repository.VigRTCConversations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.viglet.social.rtc.exception.VigRTCExceptions.MEMBER_IN_OTHER_CONVERSATION;

@Component(VigRTCSignals.CREATE_HANDLER)
public class VigRTCCreateConversation implements VigRTCSignalHandler {

    @Autowired
    private VigRTCConversations conversations;

    public void execute(VigRTCInternalMessage context) {
        conversations.findBy(context.getFrom())
                .map(VigRTCConversation::getId)
                .map(MEMBER_IN_OTHER_CONVERSATION::exception)
                .ifPresent(VigRTCSignalingException::throwException);


                VigRTCConversation conversation = conversations.create(context);

        conversation.join(context.getFrom());
    }

}
