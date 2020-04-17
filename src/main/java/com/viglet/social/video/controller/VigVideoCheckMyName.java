package com.viglet.social.video.controller;

import com.viglet.social.video.auth.VigVideoAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/action/check")
public class VigVideoCheckMyName {

    @Autowired
    private VigVideoAuthUtils authUtils;

    @RequestMapping(value = "name", method = RequestMethod.GET)
    public String check(Principal authentication) {
        return authUtils.getAuthenticatedUser(authentication).getUsername();
    }
}
