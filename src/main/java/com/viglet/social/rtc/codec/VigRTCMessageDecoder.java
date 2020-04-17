package com.viglet.social.rtc.codec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.viglet.social.rtc.domain.VigRTCMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.Map;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

public class VigRTCMessageDecoder implements Decoder.Text<VigRTCMessage> {

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @Override
    public void destroy() {
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public VigRTCMessage decode(String json) throws DecodeException {
        return gson.fromJson(escapeHtml4(json.replace("\"", "'")), VigRTCMessage.class);
    }

    @Override
    public boolean willDecode(String json) {
        return tryParse(json.replace("\"", "'"));
    }

    @SuppressWarnings("unchecked")
    private boolean tryParse(String json) {
        try {
            Map<String, String> object = gson.fromJson(json, Map.class);
            return hasAllRequiredField(object);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasAllRequiredField(Map<String, String> object) {
        return object.containsKey("signal");
    }
}
