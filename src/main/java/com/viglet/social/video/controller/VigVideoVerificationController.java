package com.viglet.social.video.controller;

import com.viglet.social.video.service.VigVideoVerifyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/action/verify")
public class VigVideoVerificationController {

    @Autowired
    private VigVideoVerifyUserService service;

    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public String verify(@PathVariable String key, Model model) {
        return "" + service.verify(key);
    }
}
