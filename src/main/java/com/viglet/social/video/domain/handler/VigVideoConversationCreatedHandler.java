package com.viglet.social.video.domain.handler;

import org.apache.log4j.Logger;

import com.viglet.social.video.domain.handler.common.VigVideoConversationHandler;
import com.viglet.social.persistence.model.video.VigVideoConversation;
import com.viglet.social.persistence.repository.video.VigVideoConversationRepository;
import com.viglet.social.rtc.api.annotation.VigRTCEventListener;
import com.viglet.social.rtc.api.dto.VigRTCConversationDTO;
import com.viglet.social.rtc.api.dto.VigRTCEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.viglet.social.rtc.api.VigRTCEvents.CONVERSATION_CREATED;

@Component
@VigRTCEventListener(CONVERSATION_CREATED)
public class VigVideoConversationCreatedHandler extends VigVideoConversationHandler {

    private static final Logger log = Logger.getLogger(VigVideoConversationCreatedHandler.class);
    @Autowired
    private VigVideoConversationRepository repo;

    @Override
    protected void handleEvent(VigRTCConversationDTO rtcConversation, VigRTCEvent event) {
        VigVideoConversation conversation = repo.getByConversationName(rtcConversation.getId());
        if (conversation == null) {
            log.info("Created conversation: " + repo.save(new VigVideoConversation(rtcConversation.getId())));
        }
    }
}
