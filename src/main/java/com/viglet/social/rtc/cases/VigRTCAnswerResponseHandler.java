package com.viglet.social.rtc.cases;

import com.viglet.social.rtc.domain.VigRTCConversation;
import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.domain.VigRTCSignals;
import org.springframework.stereotype.Component;

@Component(VigRTCSignals.ANSWER_RESPONSE_HANDLER)
public class VigRTCAnswerResponseHandler extends VigRTCExchange {

    @Override
    protected void exchange(VigRTCInternalMessage message, VigRTCConversation conversation) {
        conversation.exchangeSignals(message);
    }
}
