package com.viglet.social.video.controller;

import com.viglet.social.video.service.VigVideoRegisterUserService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/action")
public class VigVideoRegisterController {

    @Autowired
    private VigVideoRegisterUserService service;

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("username") String username, @ModelAttribute("password") String password, @ModelAttribute("email") String email, HttpServletRequest request) {
        String confirmationKey = service.generateConfirmationKey();
        service.register(username, password, email, confirmationKey);
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        return scheme + "://" + serverName + ":" + serverPort + "/action/verify/" + confirmationKey;
    }
}
