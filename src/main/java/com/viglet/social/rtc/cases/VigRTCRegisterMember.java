package com.viglet.social.rtc.cases;

import com.viglet.social.rtc.VigRTCNames;
import com.viglet.social.rtc.domain.VigRTCMember;
import com.viglet.social.rtc.domain.VigRTCPingTask;
import com.viglet.social.rtc.repository.VigRTCMembers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component()
public class VigRTCRegisterMember {

    @Value(VigRTCNames.SCHEDULED_PERIOD)
    private int period;

    @Autowired
    private VigRTCMembers members;

    @Autowired
    @Qualifier(VigRTCNames.SCHEDULER_NAME)
    private ScheduledExecutorService scheduler;

    @Autowired
    private ApplicationContext context;

    public void incoming(Session session) {
        members.register(context.getBean(VigRTCMember.class, session, ping(session)));
    }

    private ScheduledFuture<?> ping(Session session) {
        return scheduler.scheduleAtFixedRate(new VigRTCPingTask(session), period, period, TimeUnit.SECONDS);
    }

}
