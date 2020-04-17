package com.viglet.social.rtc.eventbus;

import com.viglet.social.rtc.VigRTCNames;
import com.viglet.social.rtc.api.VigRTCEventBus;
import com.viglet.social.rtc.api.annotation.VigRTCEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component("VigRTCEventBusSetup")
@Scope("singleton")
public class VigRTCEventBusSetup {

    @Autowired
    @Qualifier(VigRTCNames.EVENT_BUS)
    private VigRTCEventBus eventBus;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void setupHandlers() {
        context.getBeansWithAnnotation(VigRTCEventListener.class).values()
                .forEach(eventBus::register);
    }
}
