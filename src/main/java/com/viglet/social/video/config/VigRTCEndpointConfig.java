package com.viglet.social.video.config;

import com.viglet.social.video.VigVideoMyEndpoint;
import com.viglet.social.rtc.VigRTCConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@Import(VigRTCConfig.class)
public class VigRTCEndpointConfig {

    @Bean
    public VigVideoMyEndpoint myEndpoint() {
        return new VigVideoMyEndpoint();
    }


    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}