package com.viglet.social.video.domain.handler;

import com.viglet.social.video.domain.handler.common.VigVideoConversationHandler;
import com.viglet.social.video.service.VigVideoMemberLeftService;
import com.viglet.social.rtc.api.annotation.VigRTCEventListener;
import com.viglet.social.rtc.api.dto.VigRTCConversationDTO;
import com.viglet.social.rtc.api.dto.VigRTCEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.viglet.social.rtc.api.VigRTCEvents.MEMBER_LEFT;

@Component
@VigRTCEventListener(MEMBER_LEFT)
public class VigVideoMemberLeftHandler extends VigVideoConversationHandler {

    @Autowired
    private VigVideoMemberLeftService service;

    @Override
    protected void handleEvent(VigRTCConversationDTO conversation, VigRTCEvent event) {
        event.from().ifPresent(member ->
                service.execute(member.getId(), conversation.getId())
        );
    }
}
