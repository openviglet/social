package com.viglet.social.video;

import com.viglet.social.rtc.api.VigRTCEndpoint;
import com.viglet.social.rtc.codec.VigRTCMessageDecoder;
import com.viglet.social.rtc.codec.VigRTCMessageEncoder;

import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/signaling",//
        decoders = VigRTCMessageDecoder.class,//
        encoders = VigRTCMessageEncoder.class)
public class VigVideoMyEndpoint extends VigRTCEndpoint {
}
