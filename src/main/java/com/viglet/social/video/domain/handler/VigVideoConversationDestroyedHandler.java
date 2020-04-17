package com.viglet.social.video.domain.handler;

import com.viglet.social.video.domain.handler.common.VigVideoConversationHandler;
import com.viglet.social.video.service.VigVideoDestroyConversationService;
import com.viglet.social.rtc.api.annotation.VigRTCEventListener;
import com.viglet.social.rtc.api.dto.VigRTCConversationDTO;
import com.viglet.social.rtc.api.dto.VigRTCEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.viglet.social.rtc.api.VigRTCEvents.CONVERSATION_DESTROYED;

@Component
@VigRTCEventListener(CONVERSATION_DESTROYED)
public class VigVideoConversationDestroyedHandler extends VigVideoConversationHandler {

    @Autowired
    private VigVideoDestroyConversationService service;

    @Override
    protected void handleEvent(VigRTCConversationDTO rtcConversation, VigRTCEvent event) {
        service.execute(rtcConversation.getId());
    }
}
