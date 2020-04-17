package com.viglet.social.video.domain.handler;


import com.viglet.social.video.domain.handler.common.VigVideoFromMemberHandler;
import com.viglet.social.video.service.VigVideoSessionClosedService;
import com.viglet.social.rtc.api.annotation.VigRTCEventListener;
import com.viglet.social.rtc.api.dto.VigRTCEvent;
import com.viglet.social.rtc.api.dto.VigRTCMemberDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.viglet.social.rtc.api.VigRTCEvents.SESSION_CLOSED;

@Component
@VigRTCEventListener(SESSION_CLOSED)
public class VigVideoSessionClosedHandler extends VigVideoFromMemberHandler {

    @Autowired
    private VigVideoSessionClosedService service;

    @Override
    protected void handleEvent(VigRTCMemberDTO from, VigRTCEvent event) {
        service.execute(from.getId(), event.reason());
    }

}
