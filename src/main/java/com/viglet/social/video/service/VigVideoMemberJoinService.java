package com.viglet.social.video.service;

import com.viglet.social.persistence.model.video.VigVideoConversation;
import com.viglet.social.persistence.model.video.VigVideoMember;
import com.viglet.social.persistence.repository.video.VigVideoConversationRepository;
import com.viglet.social.persistence.repository.video.VigVideoMemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VigVideoMemberJoinService {

    @Autowired
    private VigVideoMemberRepository memberRepository;

    @Autowired
    private VigVideoConversationRepository conversationRepository;

    public void execute(String memberId, String conversationId) {
        VigVideoConversation conversation = conversationRepository.getByConversationName(conversationId);
        VigVideoMember member = memberRepository.getByRtcId(memberId);

        conversation.join(member);
    }
}
