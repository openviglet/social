package com.viglet.social.video.domain.handler.common;

import com.viglet.social.rtc.api.VigRTCHandler;
import com.viglet.social.rtc.api.dto.VigRTCEvent;
import com.viglet.social.rtc.api.dto.VigRTCMemberDTO;

public abstract class VigVideoFromMemberHandler implements VigRTCHandler {

    @Override
    public final void handleEvent(VigRTCEvent event) {
        event.from().ifPresent(from -> handleEvent(from, event));
    }

    protected abstract void handleEvent(VigRTCMemberDTO from, VigRTCEvent event);
}
