package com.viglet.social.rtc.api;

import com.viglet.social.rtc.api.dto.VigRTCEvent;

public interface VigRTCHandler {

    void handleEvent(VigRTCEvent event);

}
