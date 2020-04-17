package com.viglet.social.rtc.domain;

import javax.websocket.Session;

public class VigRTCPingTask implements Runnable {

    private VigRTCMember to;

    public VigRTCPingTask(Session to) {
        this.to = new VigRTCMember(to, null);
    }

    @Override
    public void run() {
        VigRTCInternalMessage.create()//
                .to(to)//
                .signal(VigRTCSignal.PING)//
                .build()//
                .sendCarefully();
    }

}
