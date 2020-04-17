package com.viglet.social.video.domain.handler;

import com.viglet.social.video.domain.handler.common.VigVideoFromMemberHandler;
import com.viglet.social.video.service.VigVideoSessionOpenedService;
import com.viglet.social.rtc.api.annotation.VigRTCEventListener;
import com.viglet.social.rtc.api.dto.VigRTCEvent;
import com.viglet.social.rtc.api.dto.VigRTCMemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.viglet.social.rtc.api.VigRTCEvents.SESSION_OPENED;

@Service
@VigRTCEventListener(SESSION_OPENED)
public class VigVideoSessionOpenedHandler extends VigVideoFromMemberHandler {

    @Autowired
    private VigVideoSessionOpenedService service;

    @Override
    protected void handleEvent(VigRTCMemberDTO from, VigRTCEvent event) {
        service.execute(from);
    }
}
