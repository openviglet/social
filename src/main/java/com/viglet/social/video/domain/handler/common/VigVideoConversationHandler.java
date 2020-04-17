package com.viglet.social.video.domain.handler.common;

import com.viglet.social.rtc.api.VigRTCHandler;
import com.viglet.social.rtc.api.dto.VigRTCConversationDTO;
import com.viglet.social.rtc.api.dto.VigRTCEvent;

public abstract class VigVideoConversationHandler implements VigRTCHandler {

    @Override
    public final void handleEvent(VigRTCEvent event) {
        event.conversation().ifPresent(conversation -> handleEvent(conversation, event));
    }

    protected abstract void handleEvent(VigRTCConversationDTO conversation, VigRTCEvent event);
}
