package com.viglet.social.rtc.codec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.viglet.social.rtc.domain.VigRTCMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class VigRTCMessageEncoder implements Encoder.Text<VigRTCMessage> {

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @Override
    public void destroy() {
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public String encode(VigRTCMessage message) throws EncodeException {
        return gson.toJson(message);
    }
}
