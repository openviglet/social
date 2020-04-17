package com.viglet.social.rtc.eventbus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.apache.log4j.Logger;
import com.viglet.social.rtc.VigRTCNames;
import com.viglet.social.rtc.api.VigRTCEvents;
import com.viglet.social.rtc.api.VigRTCHandler;
import com.viglet.social.rtc.api.annotation.VigRTCEventListener;
import com.viglet.social.rtc.api.dto.VigRTCEvent;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

import static org.springframework.core.annotation.AnnotationUtils.getValue;

@Component(VigRTCNames.EVENT_DISPATCHER)
@Scope("singleton")
@VigRTCEventListener
public class VigRTCEventDispatcher {
    private static final Logger log = Logger.getLogger(VigRTCEventDispatcher.class);

    @Autowired
    private ApplicationContext context;

    @Subscribe
    @AllowConcurrentEvents
    public void handle(VigRTCEvent event) {
        getVigRTCEventListeners().stream()
                .filter(listener -> isVigRTCHandler(listener) && supportsCurrentEvent(listener, event))
                .forEach(listener -> doTry(() -> ((VigRTCHandler) listener).handleEvent(event)));

    }

    private void doTry(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            log.error("Handler throws an exception", e);
        }
    }

    private boolean isVigRTCHandler(Object listener) {
        return listener instanceof VigRTCHandler;
    }

    private boolean supportsCurrentEvent(Object listener, VigRTCEvent event) {
        VigRTCEvents[] events = getSupportedEvents(listener);
        for (VigRTCEvents supportedEvent : events) {
            if (isSupporting(event, supportedEvent)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSupporting(VigRTCEvent msg, VigRTCEvents supportedEvent) {
        return supportedEvent.equals(msg.type());
    }

    private VigRTCEvents[] getSupportedEvents(Object listener) {
        try {
            if (AopUtils.isJdkDynamicProxy(listener)) {
                listener = ((Advised) listener).getTargetSource().getTarget();
            }
        } catch (Exception e) {
            return new VigRTCEvents[0];
        }
        return (VigRTCEvents[]) getValue(listener.getClass().getAnnotation(VigRTCEventListener.class));
    }

    private Collection<Object> getVigRTCEventListeners() {
        Map<String, Object> beans = context.getBeansWithAnnotation(VigRTCEventListener.class);
        beans.remove(VigRTCNames.EVENT_DISPATCHER);
        return beans.values();
    }
}
