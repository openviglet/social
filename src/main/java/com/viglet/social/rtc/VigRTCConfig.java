package com.viglet.social.rtc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;

import java.util.concurrent.ScheduledExecutorService;

@Configuration
@ComponentScan(basePackageClasses = {VigRTCConfig.class})
public class VigRTCConfig {

    @Value(VigRTCNames.SCHEDULER_SIZE)
    private int size;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocation(new ClassPathResource("vigrtc.properties"));
        return propertyPlaceholderConfigurer;
    }

    @Bean(name = VigRTCNames.SCHEDULER_NAME)
    public ScheduledExecutorService scheduler() {
        ScheduledExecutorFactoryBean factoryBean = new ScheduledExecutorFactoryBean();
        factoryBean.setThreadNamePrefix("VigRTCConfig");
        factoryBean.setPoolSize(size);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}
