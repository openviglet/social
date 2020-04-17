package com.viglet.social.video.controller;

import com.viglet.social.video.domain.history.VigVideoHistory;
import com.viglet.social.video.service.VigVideoHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/action/history")
public class VigVideoHistoryController {

    @Autowired
    private VigVideoHistoryService historyService;

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public VigVideoHistory getHistory(Principal authentication) {
        return historyService.getHistoryFor(authentication);
    }


}
