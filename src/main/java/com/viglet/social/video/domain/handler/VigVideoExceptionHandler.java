package com.viglet.social.video.domain.handler;

import org.apache.log4j.Logger;
import com.viglet.social.rtc.api.VigRTCHandler;
import com.viglet.social.rtc.api.annotation.VigRTCEventListener;
import com.viglet.social.rtc.api.dto.VigRTCEvent;
import org.springframework.stereotype.Component;

import static com.viglet.social.rtc.api.VigRTCEvents.UNEXPECTED_SITUATION;

@Component
@VigRTCEventListener(UNEXPECTED_SITUATION)
public class VigVideoExceptionHandler implements VigRTCHandler {

    private static final Logger log = Logger.getLogger(VigVideoExceptionHandler.class);

    @Override
    public void handleEvent(VigRTCEvent VigRTCEvent) {
        log.error(VigRTCEvent);
    }
}
