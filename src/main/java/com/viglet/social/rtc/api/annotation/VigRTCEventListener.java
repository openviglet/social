package com.viglet.social.rtc.api.annotation;

import com.viglet.social.rtc.api.VigRTCEvents;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE})
public @interface VigRTCEventListener {

    VigRTCEvents[] value() default {};
}
