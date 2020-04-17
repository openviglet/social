package com.viglet.social.rtc.api.dto;

import org.joda.time.DateTime;
import com.viglet.social.rtc.api.VigRTCEvents;
import com.viglet.social.rtc.exception.VigRTCSignalingException;

import java.util.Map;
import java.util.Optional;

public interface VigRTCEvent {

    VigRTCEvents type();

    DateTime published();

    Optional<VigRTCMemberDTO> from();

    Optional<VigRTCMemberDTO> to();

    Optional<VigRTCConversationDTO> conversation();

    Optional<VigRTCSignalingException> exception();

    Map<String, String> custom();

    Optional<String> content();

    Optional<String> reason();

}
