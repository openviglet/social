package com.viglet.social.rtc.domain;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import com.viglet.social.rtc.cases.VigRTCRegisterMember;
import com.viglet.social.rtc.cases.VigRTCSignalHandler;
import com.viglet.social.rtc.domain.VigRTCInternalMessage.VigRTCInternalMessageBuilder;
import com.viglet.social.rtc.exception.VigRTCSignalingException;
import com.viglet.social.rtc.repository.VigRTCMembers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Consumer;

import static com.viglet.social.rtc.exception.VigRTCExceptions.MEMBER_NOT_FOUND;

@Component
public class VigRTCServer {

    private static final Logger log = Logger.getLogger(VigRTCServer.class);
    @Autowired
    private VigRTCMembers members;

    @Autowired
    private VigRTCSignalResolver resolver;

    @Autowired
    private VigRTCRegisterMember register;

    public void register(Session s) {
        doSaveExecution(s, session ->
                register.incoming(session)
        );
    }

    public void handle(VigRTCMessage external, Session s) {
        doSaveExecution(s, session -> {
            Pair<VigRTCSignal, VigRTCSignalHandler> resolve = resolver.resolve(external.getSignal());
            VigRTCInternalMessage internalMessage = buildInternalMessage(external, resolve.getKey(), session);
            processMessage(resolve.getValue(), internalMessage);
        });
    }

    private void processMessage(VigRTCSignalHandler handler, VigRTCInternalMessage message) {
        log.info("Incoming: " + message);
        if (handler != null) {
            handler.execute(message);
        }
    }

    private VigRTCInternalMessage buildInternalMessage(VigRTCMessage message, VigRTCSignal signal, Session session) {
        VigRTCInternalMessageBuilder bld = VigRTCInternalMessage.create()//
                .from(findMember(session))//
                .content(message.getContent())//
                .signal(signal)//
                .custom(message.getCustom());
        members.findBy(message.getTo()).ifPresent(bld::to);
        return bld.build();
    }

    private VigRTCMember findMember(Session session) {
        return members.findBy(session.getId()).orElseThrow(() -> new VigRTCSignalingException(MEMBER_NOT_FOUND));
    }

    public void unregister(Session s, CloseReason reason) {
        doSaveExecution(s, session ->
                members.unregisterBy(session, reason.getReasonPhrase())
        );
    }


    public void handleError(Session s, Throwable exception) {
        doSaveExecution(s, session ->
                members.dropOutAfterException(session, exception.getMessage())
        );
    }

    private void doSaveExecution(Session session, Consumer<Session> action) {
        try {
            action.accept(session);
        } catch (Exception e) {
            log.warn("Server will try to handle this exception and send information as normal message through websocket", e);
            sendErrorOverWebSocket(session, e);
        }
    }

    private void sendErrorOverWebSocket(Session session, Exception e) {
        try {
            VigRTCInternalMessage.create()
                    .to(new VigRTCMember(session, null))
                    .signal(VigRTCSignal.ERROR)
                    .content(e.getMessage())
                    .addCustom("stackTrace", writeStackTraceToString(e))
                    .build()
                    .send();
        } catch (Exception resendException) {
            log.error("Something goes wrong during resend! Exception omitted", resendException);
        }
    }

    private String writeStackTraceToString(Exception e) {
        if (log.isDebugEnabled()) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            return errors.toString();
        }
        return e.getClass().getSimpleName() + " - " + e.getMessage();
    }

}
