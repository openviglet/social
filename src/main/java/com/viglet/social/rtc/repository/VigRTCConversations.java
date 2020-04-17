package com.viglet.social.rtc.repository;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import com.viglet.social.rtc.VigRTCNames;
import com.viglet.social.rtc.api.VigRTCEventBus;
import com.viglet.social.rtc.domain.VigRTCConversation;
import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.domain.VigRTCMember;
import com.viglet.social.rtc.domain.conversation.VigRTCBroadcastConversation;
import com.viglet.social.rtc.domain.conversation.VigRTCMeshConversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static com.viglet.social.rtc.api.VigRTCEvents.CONVERSATION_CREATED;
import static com.viglet.social.rtc.api.VigRTCEvents.CONVERSATION_DESTROYED;
import static com.viglet.social.rtc.domain.VigRTCEventContext.builder;
import static com.viglet.social.rtc.exception.VigRTCExceptions.CONVERSATION_NAME_OCCUPIED;
import static com.viglet.social.rtc.exception.VigRTCExceptions.INVALID_CONVERSATION_NAME;

@Repository
public class VigRTCConversations {

    @Autowired
    @Qualifier(VigRTCNames.EVENT_BUS)
    private VigRTCEventBus eventBus;

    @Autowired
    private ApplicationContext context;

    private Map<String, VigRTCConversation> conversations = Maps.newConcurrentMap();

    public Optional<VigRTCConversation> findBy(String id) {
        if (isEmpty(id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(conversations.get(id));
    }

    public Optional<VigRTCConversation> findBy(VigRTCMember from) {
        return conversations.values().stream().filter(conversation -> conversation.has(from)).findAny();
    }

    public void remove(String id, VigRTCMember sender) {
        eventBus.post(CONVERSATION_DESTROYED.basedOn(
                builder()
                        .conversation(conversations.remove(id))
                        .from(sender)));
    }

    public VigRTCConversation create(VigRTCInternalMessage message) {
        String conversationName = getConversationName(message.getContent());
        final VigRTCConversation conversation = create(conversationName, Optional.ofNullable(message.getCustom().get("type")));
        postEvent(message, conversation);
        return conversation;
    }

    private void postEvent(VigRTCInternalMessage message, VigRTCConversation conversation) {
        eventBus.post(CONVERSATION_CREATED.basedOn(message, conversation));
    }

    private VigRTCConversation create(String conversationName, Optional<String> optionalType) {
        String type = optionalType.orElse("MESH");
        VigRTCConversation conversation = null;
        if (type.equalsIgnoreCase("BROADCAST")) {
            conversation = context.getBean(VigRTCBroadcastConversation.class, conversationName);
        } else if (type.equalsIgnoreCase("MESH")) {
            conversation = context.getBean(VigRTCMeshConversation.class, conversationName);
        }
        registerInContainer(conversation);
        return conversation;
    }

    private String getConversationName(String name) {
        final String conversationName = StringUtils.isBlank(name) ? UUID.randomUUID().toString() : name;
        validate(conversationName);
        return conversationName;
    }

    private void registerInContainer(VigRTCConversation conversation) {
        conversations.put(conversation.getId(), conversation);
    }

    private void validate(String name) {
        if (isEmpty(name)) {
            throw INVALID_CONVERSATION_NAME.exception();
        }
        if (conversations.containsKey(name)) {
            throw CONVERSATION_NAME_OCCUPIED.exception();
        }
    }

    public Collection<String> getAllIds() {
        return conversations.keySet();
    }
}
