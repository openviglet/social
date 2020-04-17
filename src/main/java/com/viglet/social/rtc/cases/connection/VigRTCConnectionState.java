package com.viglet.social.rtc.cases.connection;

import com.viglet.social.rtc.domain.VigRTCInternalMessage;
import com.viglet.social.rtc.domain.VigRTCSignal;

public enum VigRTCConnectionState {
    OFFER_REQUESTED {
        @Override
        public boolean isValid(VigRTCInternalMessage message) {
            return VigRTCSignal.OFFER_RESPONSE.is(message.getSignal());
        }
    },
    ANSWER_REQUESTED {
        @Override
        public boolean isValid(VigRTCInternalMessage message) {
            return VigRTCSignal.ANSWER_RESPONSE.is(message.getSignal());
        }
    },
    EXCHANGE_CANDIDATES {
        @Override
        public boolean isValid(VigRTCInternalMessage message) {
            return VigRTCSignal.CANDIDATE.is(message.getSignal());
        }
    },
    NOT_INITIALIZED {
        @Override
        public boolean isValid(VigRTCInternalMessage message) {
            return false;
        }
    };


    VigRTCConnectionState() {
    }

    public abstract boolean isValid(VigRTCInternalMessage message);
}
