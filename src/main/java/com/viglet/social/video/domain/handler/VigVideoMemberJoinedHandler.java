package com.viglet.social.video.domain.handler;

import com.viglet.social.video.domain.handler.common.VigVideoFromMemberHandler;
import com.viglet.social.video.service.VigVideoMemberJoinService;
import com.viglet.social.rtc.api.annotation.VigRTCEventListener;
import com.viglet.social.rtc.api.dto.VigRTCEvent;
import com.viglet.social.rtc.api.dto.VigRTCMemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.viglet.social.rtc.api.VigRTCEvents.MEMBER_JOINED;

@Component
@VigRTCEventListener(MEMBER_JOINED)
public class VigVideoMemberJoinedHandler extends VigVideoFromMemberHandler {

    @Autowired
    private VigVideoMemberJoinService service;

    @Override
    protected void handleEvent(VigRTCMemberDTO from, VigRTCEvent event) {
        event.conversation().ifPresent(conversation -> service.execute(from.getId(), conversation.getId()));
    }
}
