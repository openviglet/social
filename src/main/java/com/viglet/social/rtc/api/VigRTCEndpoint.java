package com.viglet.social.rtc.api;

import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import com.viglet.social.rtc.domain.VigRTCMessage;
import com.viglet.social.rtc.domain.VigRTCServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.util.Set;

@Component
public class VigRTCEndpoint {

    private static final Logger log = Logger.getLogger(VigRTCEndpoint.class);
    private VigRTCServer server;

    private static Set<VigRTCEndpoint> endpoints = Sets.newConcurrentHashSet();

    public VigRTCEndpoint() {
        endpoints.add(this);
        log.info("Created " + this);
        endpoints.stream().filter(e -> e.server != null).findFirst().ifPresent(s -> this.setServer(s.server));
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        log.info("Opening: " + session.getId());
        server.register(session);
    }

    @OnMessage
    public void onMessage(VigRTCMessage message, Session session) {
        log.info("Handling message from: " + session.getId());
        server.handle(message, session);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        log.info("Closing: " + session.getId() + " with reason: " + reason.getReasonPhrase());
        server.unregister(session, reason);
    }

    @OnError
    public void onError(Session session, Throwable exception) {
        log.error("Occured exception for session: " + session.getId() + ", reason: " + exception.getMessage());
        log.debug("Endpoint exception: ", exception);
        server.handleError(session, exception);
    }

    @Autowired
    public void setServer(VigRTCServer server) {
        log.info("Setted server: " + server + " to " + this);
        this.server = server;
    }
}
