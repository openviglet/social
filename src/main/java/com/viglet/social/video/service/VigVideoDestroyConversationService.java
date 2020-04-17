package com.viglet.social.video.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viglet.social.persistence.repository.video.VigVideoConversationRepository;

@Service
@Transactional
public class VigVideoDestroyConversationService {

    @Autowired
    private VigVideoConversationRepository conversationRepository;

    public void execute(String roomName) {
        conversationRepository.getByConversationName(roomName).destroy();
    }
}
