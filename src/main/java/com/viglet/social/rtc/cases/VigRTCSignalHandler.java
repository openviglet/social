package com.viglet.social.rtc.cases;

import com.viglet.social.rtc.domain.VigRTCInternalMessage;

public interface VigRTCSignalHandler {
    void execute(VigRTCInternalMessage message);
}
