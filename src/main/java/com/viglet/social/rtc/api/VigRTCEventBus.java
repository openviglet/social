package com.viglet.social.rtc.api;

import com.google.common.eventbus.EventBus;
import org.apache.log4j.Logger;
import com.viglet.social.rtc.VigRTCNames;
import com.viglet.social.rtc.api.dto.VigRTCEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service(VigRTCNames.EVENT_BUS)
@Scope("singleton")
public class VigRTCEventBus {

    private static final Logger log = Logger.getLogger(VigRTCEventBus.class);
    private EventBus eventBus;

    public VigRTCEventBus() {
        this.eventBus = new EventBus();
    }

    public void post(VigRTCEvent event) {
        log.info("POSTED EVENT: " + event);
        eventBus.post(event);
    }

    @Deprecated
    public void post(Object o) {
        eventBus.post(o);
    }

    public void register(Object listeners) {
        log.info("REGISTERED LISTENER: " + listeners);
        eventBus.register(listeners);
    }

}
