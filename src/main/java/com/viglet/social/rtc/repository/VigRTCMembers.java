package com.viglet.social.rtc.repository;

import com.google.common.collect.Maps;
import com.viglet.social.rtc.VigRTCNames;
import com.viglet.social.rtc.api.VigRTCEventBus;
import com.viglet.social.rtc.domain.VigRTCMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.websocket.Session;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.viglet.social.rtc.api.VigRTCEvents.*;

@Repository
public class VigRTCMembers {

    @Autowired
    @Qualifier(VigRTCNames.EVENT_BUS)
    private VigRTCEventBus eventBus;

    private Map<String, VigRTCMember> members = Maps.newConcurrentMap();

    public Collection<String> getAllIds() {
        return members.keySet();
    }

    public Optional<VigRTCMember> findBy(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(members.get(id));
    }

    public void register(VigRTCMember member) {
        members.computeIfAbsent(member.getId(), put -> member);
        eventBus.post(SESSION_OPENED.occurFor(member.getSession()));
    }

    public void unregisterBy(Session session, String reason) {
        unregister(session.getId());
        eventBus.post(SESSION_CLOSED.occurFor(session, reason));
    }

    private void unregister(String id) {
        findBy(id).ifPresent(VigRTCMember::markLeft);
        VigRTCMember removed = members.remove(id);
        if (removed != null) {
            removed.getConversation().ifPresent(c -> c.left(removed));
        }
    }

    public void dropOutAfterException(Session session, String reason) {
        unregister(session.getId());
        eventBus.post(UNEXPECTED_SITUATION.occurFor(session, reason));
    }
}
