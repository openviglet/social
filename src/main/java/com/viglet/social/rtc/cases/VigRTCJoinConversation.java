package com.viglet.social.rtc.cases;

import com.viglet.social.rtc.domain.VigRTCConversation;
import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.domain.VigRTCSignals;
import com.viglet.social.rtc.exception.VigRTCSignalingException;
import com.viglet.social.rtc.repository.VigRTCConversations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.viglet.social.rtc.exception.VigRTCExceptions.MEMBER_IN_OTHER_CONVERSATION;

@Component(VigRTCSignals.JOIN_HANDLER)
public class VigRTCJoinConversation implements VigRTCSignalHandler {

    @Autowired
    private VigRTCConversations conversations;

    @Autowired
    @Qualifier(VigRTCSignals.CREATE_HANDLER)
    private VigRTCCreateConversation createConversation;

    public void execute(VigRTCInternalMessage context) {
        conversations.findBy(context.getFrom())
                .map(VigRTCConversation::getId)
                .map(MEMBER_IN_OTHER_CONVERSATION::exception)
                .ifPresent(VigRTCSignalingException::throwException);

        Optional<VigRTCConversation> conversation = findConversationToJoin(context);
        if (conversation.isPresent()) {
            conversation.get().join(context.getFrom());
        } else {
            createConversation.execute(context);
        }
    }

    private Optional<VigRTCConversation> findConversationToJoin(VigRTCInternalMessage message) {
        return conversations.findBy(message.getContent());
    }

}
