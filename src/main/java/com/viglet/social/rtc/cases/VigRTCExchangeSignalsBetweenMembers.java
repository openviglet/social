package com.viglet.social.rtc.cases;

import com.viglet.social.rtc.cases.connection.VigRTCConnectionContext;
import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.domain.VigRTCMember;
import com.viglet.social.rtc.domain.VigRTCConnections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class VigRTCExchangeSignalsBetweenMembers {

    @Autowired
    private VigRTCConnections connections;

    @Autowired
    private ApplicationContext context;

    public synchronized void begin(VigRTCMember from, VigRTCMember to) {
        connections.put(from, to, context.getBean(VigRTCConnectionContext.class, from, to));
        connections.get(from, to).ifPresent(VigRTCConnectionContext::begin);
    }

    public synchronized void execute(VigRTCInternalMessage message) {
        connections.get(message.getFrom(), message.getTo()).ifPresent(context -> context.process(message));
    }
}
