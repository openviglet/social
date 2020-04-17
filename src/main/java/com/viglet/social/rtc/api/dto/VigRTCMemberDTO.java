package com.viglet.social.rtc.api.dto;

import javax.websocket.Session;

public interface VigRTCMemberDTO {
    default String getId() {
        return getSession().getId();
    }

    Session getSession();
}
