package com.viglet.social.rtc.domain;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import com.viglet.social.rtc.cases.VigRTCSignalHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Component
@Scope("singleton")
public class VigRTCSignalResolver implements InitializingBean {

    @Autowired
    private Map<String, VigRTCSignalHandler> handlers;

    private Map<VigRTCSignal, VigRTCSignalHandler> customHandlers = new HashMap<>();

    public Pair<VigRTCSignal, VigRTCSignalHandler> resolve(String string) {
        VigRTCSignal signal = VigRTCSignal.fromString(string);
        if (customHandlers.containsKey(signal)) {
            return new ImmutablePair<>(signal, customHandlers.get(signal));
        }
        return new ImmutablePair<>(VigRTCSignal.EMPTY, handlers.get(VigRTCSignal.EMPTY.handlerName()));
    }

    public Optional<Pair<VigRTCSignal, VigRTCSignalHandler>> addCustomHandler(VigRTCSignal signal, VigRTCSignalHandler handler) {
        VigRTCSignalHandler oldValue = customHandlers.put(signal, handler);
        if (oldValue == null) {
            return Optional.empty();
        }
        return Optional.of(new ImmutablePair<>(signal, handler));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (VigRTCSignal signal : VigRTCSignal.values()) {
            addCustomHandler(signal, getHandler(signal));
        }
    }

    private VigRTCSignalHandler getHandler(VigRTCSignal signal) {
        return ofNullable(handlers.get(signal.handlerName())).orElse(handlers.get(VigRTCSignal.EMPTY.handlerName()));
    }
}
