package com.viglet.social.rtc.cases.connection;

import org.joda.time.DateTime;
import com.viglet.social.rtc.VigRTCNames;
import com.viglet.social.rtc.api.VigRTCEventBus;
import com.viglet.social.rtc.api.VigRTCEvents;
import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.domain.VigRTCMember;
import com.viglet.social.rtc.domain.VigRTCSignal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class VigRTCConnectionContext {

    @Value(VigRTCNames.MAX_CONNECTION_SETUP_TIME)
    private int maxConnectionSetupTime;

    private VigRTCConnectionState state = VigRTCConnectionState.NOT_INITIALIZED;
    private DateTime lastUpdated = DateTime.now();

    @Autowired
    @Qualifier(VigRTCNames.EVENT_BUS)
    private VigRTCEventBus bus;

    private VigRTCMember master;
    private VigRTCMember slave;

    public VigRTCConnectionContext(VigRTCMember master, VigRTCMember slave) {
        this.master = master;
        this.slave = slave;
    }


    public void process(VigRTCInternalMessage message) {
        if (is(message, VigRTCConnectionState.OFFER_REQUESTED)) {
            setState(VigRTCConnectionState.ANSWER_REQUESTED);
            answerRequest(message);
        } else if (is(message, VigRTCConnectionState.ANSWER_REQUESTED)) {
            setState(VigRTCConnectionState.EXCHANGE_CANDIDATES);
            finalize(message);
        } else if (is(message, VigRTCConnectionState.EXCHANGE_CANDIDATES)) {
            exchangeCandidates(message);
        }
    }


    private void exchangeCandidates(VigRTCInternalMessage message) {
        message.copy().signal(VigRTCSignal.CANDIDATE).build().send();
    }


    private void finalize(VigRTCInternalMessage message) {
        message.copy()//
                .from(slave)//
                .to(master)//
                .signal(VigRTCSignal.FINALIZE)//
                .build()//
                .send();
        bus.post(VigRTCEvents.MEDIA_LOCAL_STREAM_CREATED.occurFor(slave.getSession()));
        bus.post(VigRTCEvents.MEDIA_STREAMING.occurFor(master.getSession()));
        bus.post(VigRTCEvents.MEDIA_STREAMING.occurFor(slave.getSession()));
    }


    private void answerRequest(VigRTCInternalMessage message) {
        bus.post(VigRTCEvents.MEDIA_LOCAL_STREAM_CREATED.occurFor(master.getSession()));
        message.copy()//
                .from(master)//
                .to(slave)//
                .signal(VigRTCSignal.ANSWER_REQUEST)//
                .build()//
                .send();
        bus.post(VigRTCEvents.MEDIA_LOCAL_STREAM_REQUESTED.occurFor(slave.getSession()));
    }

    private boolean is(VigRTCInternalMessage message, VigRTCConnectionState state) {
        return state.equals(this.state) && state.isValid(message);
    }

    public void begin() {
        setState(VigRTCConnectionState.OFFER_REQUESTED);
        VigRTCInternalMessage.create()//
                .from(slave)//
                .to(master)//
                .signal(VigRTCSignal.OFFER_REQUEST)
                .build()//
                .send();
        bus.post(VigRTCEvents.MEDIA_LOCAL_STREAM_REQUESTED.occurFor(master.getSession()));
    }

    public boolean isCurrent() {
        return lastUpdated.plusSeconds(maxConnectionSetupTime).isAfter(DateTime.now());
    }

    public VigRTCMember getMaster() {
        return master;
    }

    public VigRTCMember getSlave() {
        return slave;
    }

    public VigRTCConnectionState getState() {
        return state;
    }

    private void setState(VigRTCConnectionState state) {
        this.state = state;
        lastUpdated = DateTime.now();
    }
}
